# Booky: Datomic and PostgreSQL side by side

## 關於這個 repo
- 比較說明文章: doc/index.md
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


## 其它參考資料

1. [Datomic 作為一種高階資料庫](https://replware.substack.com/p/datomic)
2. [Datomic: 內建事件溯源的資料庫](https://ithelp.ithome.com.tw/users/20161869/ironman/7432)
