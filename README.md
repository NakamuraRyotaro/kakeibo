# kakeibo

一人暮らしの大学生向け支出管理アプリ。「今月あと使えるお金」を常に見えるようにし、月の途中で使いすぎにブレーキをかけることを目的とする。

Java / Spring Boot / AWS を中心とした技術スタックの体系的な学習を目的とした個人開発プロジェクト。題材は学習項目(トランザクション、定期実行、キャッシュ、集計クエリ等)が必然的に必要になることを基準に選定した。

## 技術スタック

| 領域 | 技術 |
| --- | --- |
| フロントエンド | React 19 / TypeScript / Vite / pnpm |
| バックエンド | Java 21 (LTS) / Spring Boot 4.x / Gradle (Kotlin DSL) |
| DB | PostgreSQL 17 / Spring Data JPA / Flyway(導入予定) |
| 認証 | Amazon Cognito + JWT / Spring Security(導入予定) |
| インフラ | AWS: S3 + CloudFront / ECS Fargate + ALB / RDS / EventBridge + Lambda(導入予定) |
| CI/CD | GitHub Actions + OIDC(構築予定) |

DB に PostgreSQL を採用しているのは、v2 で構想している Personalized RAG を pgvector 拡張により追加コストなく同一 DB 内で実現できるため。

## ディレクトリ構成

    kakeibo/
    ├── frontend/          # Vite + React + TypeScript
    ├── backend/           # Spring Boot API
    ├── docs/              # 設計ドキュメント(ER図、構成図、構築手順)
    └── compose.yaml       # ローカル開発用 PostgreSQL

## ローカル開発環境の起動

前提: Java 21 / Node 22 / Docker Desktop(導入手順は docs/環境構築.md を参照)

```bash
# 1. DB 起動
docker compose up -d

# 2. バックエンド起動(http://localhost:8080)
cd backend
./gradlew bootRun

# 3. フロントエンド起動(http://localhost:5173)
cd frontend
pnpm install
pnpm dev
```

動作確認:

```bash
curl -s http://localhost:8080/actuator/health   # => {"status":"UP"}
```

## アーキテクチャ

```
                        ┌─ /*      → S3(React)
ブラウザ → CloudFront ─┤
                        └─ /api/*  → ALB → ECS Fargate(Spring Boot)→ RDS
```

- ECS タスクと RDS は private subnet に配置し、外部への通信は VPC エンドポイント経由(NAT Gateway 不使用)
- セキュリティグループは SG 参照でチェーン(ALB ← インターネット / ECS ← ALB / RDS ← ECS)
- 2 AZ 構成の土台に対し、実体は最小(ECS desired: 1、RDS Single-AZ)

詳細は docs/ の構成図および構築手順を参照。

## 開発の進め方

SIer の工程を模した進行で開発している:

構想・要件定義 → 基本設計(画面 / ER 図 / API)→ 環境構築 → Walking Skeleton(インフラ・CI/CD の骨格を先行デプロイ)→ 実装(単体テスト並走)→ 結合テスト・非機能改善 → 運用

現在: **Walking Skeleton 構築中(Step 4 / 全 5 ステップ)**

## ステータス

- [x] 構想・要件定義(ターゲット定義、MVP 機能選定)
- [x] 基本設計(画面設計 9 画面、ER 図 5 テーブル、API 設計、AWS 構成図)
- [x] 環境構築(ローカル: Spring Boot + PostgreSQL + Vite)
- [ ] Walking Skeleton
  - [x] Step 0: IAM / MFA / Budgets
  - [x] Step 1: S3 + CloudFront(OAC、SPA ルーティング)
  - [x] Step 2: VPC + ECR + ECS Fargate + ALB
  - [x] Step 3: CloudFront → ALB 合流(`/api/*` ビヘイビア)
  - [ ] Step 4: RDS(PostgreSQL)
  - [ ] Step 5: CI/CD(GitHub Actions + OIDC)
- [ ] 認証・認可(Cognito + JWT、JIT プロビジョニング)
- [ ] バックエンド実装(支出 CRUD、ダッシュボード集計)
- [ ] フロントエンド実装
- [ ] 結合テスト・非機能改善(計測 → インデックス → 必要ならキャッシュ)
- [ ] v1.1 定期実行(EventBridge + Lambda)/ v1.2 キャッシュ(Redis)