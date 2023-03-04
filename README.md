# ISBN ガチャ
## 概要
ISBN をランダムに生成し，書籍情報を取得するアプリ．

## 開発動機
最近読んだ本の中で，「くじ引き読書法」という方法が紹介されていた．
これは，書籍の ISBN をランダムに生成することで，これまで自分が読んでこなかった本に出会うきっかけを作るという方法である．

これを実装すれば，新たな本に出会うためのきっかけが生まれるのではないかと考え，開発した．

## 使用したライブラリやAPI
- Room
- OkHttp
- openBD API

## 工夫した点
無駄な通信を行わないようにするため，一度取得した書籍情報は DB に保存するようにし，2回目以降は DB を参照するようにした．

## 苦労した点
まだ Kotlin に慣れていないこと，非同期処理への理解が足りていないことなどの理由により，非同期処理全般に苦労した．

## 感想
想像よりも ISBN のヒット率が低かった．

## スクリーンショット
![1](https://user-images.githubusercontent.com/58594938/222890264-80feee89-eb63-4fb5-9b45-16c3b1ff62df.png)
![2](https://user-images.githubusercontent.com/58594938/222890265-1529e761-ef03-4efd-b1a5-86a404f0cc77.png)

![3](https://user-images.githubusercontent.com/58594938/222890267-f858990b-d12c-419b-ba89-b5010f0ea8df.png)
![4](https://user-images.githubusercontent.com/58594938/222890322-0dababef-982a-4683-9a7a-bc376b2699ec.png)
