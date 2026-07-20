# kakeibo

一人暮らしの大学生向け支出管理アプリ。「今月あと使えるお金」を常に見えるようにし、月の途中で使いすぎにブレーキをかけることを目的とする。

Java / Spring Boot / AWS を中心とした技術スタックの体系的な学習を目的とした個人開発プロジェクト。題材は学習項目(トランザクション、定期実行、キャッシュ、集計クエリ等)が必然的に必要になることを基準に選定した。

## 技術スタック

| 領域 | 技術 |
| --- | --- |
| フロントエンド | React 19 / TypeScript / Vite / pnpm |
| バックエンド | Java 21 (LTS) / Spring Boot 4.x / Gradle (Kotlin DSL) |
| DB | MySQL 8.x / Spring Data JPA / Flyway(導入予定) |
| 認証 | Amazon Cognito + JWT / Spring Security(導入予定) |
| インフラ | AWS: S3 + CloudFront / ECS Fargate + ALB / RDS / EventBridge + Lambda(構築予定) |
| CI/CD | GitHub Actions(構築予定) |


## ディレクトリ構成

    kakeibo/
    ├── frontend/          # Vite + React + TypeScript
    ├── backend/           # Spring Boot API
    ├── docs/              # 設計ドキュメント(ER図、画面設計、環境構築手順)
    └── compose.yaml       # ローカル開発用 MySQL


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

## 開発の進め方

SIer の工程を模した進行で開発している:

構想・要件定義 → 基本設計(画面 / ER 図 / API)→ 環境構築 → Walking Skeleton(インフラ・CI/CD の骨格を先行デプロイ)→ 実装(単体テスト並走)→ 結合テスト・非機能改善 → 運用

現在: 環境構築完了、Walking Skeleton 着手前

## ステータス

- [x] 構想・要件定義(ターゲット定義、MVP 機能選定)
- [x] 基本設計(画面設計 9 画面、ER 図 5 テーブル、API 設計)
- [x] 環境構築(ローカル: Spring Boot + MySQL + Vite)
- [ ] Walking Skeleton(AWS デプロイ + CI/CD)
- [ ] バックエンド実装(認証、支出 CRUD、ダッシュボード集計)
- [ ] フロントエンド実装
- [ ] 定期実行(EventBridge + Lambda)/ キャッシュ(Redis)
