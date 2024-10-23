# Booky: Datomic and PostgreSQL side by side

- 文章連結: doc/index.md
- 重點比較:
  - DDL:
    - SQL: 手動定義 Primary Key, 綱要定義在「表」
    - Datomic: 自動 Primary Key, 綱要定義在「欄」
  - 寫入語法
    - SQL: string 型態
    - Datomic: data 型態
  - 查詢語法
    - SQL: select ... from/join where
    - Datomic: Datalog query/ Entity API/ Pull API

