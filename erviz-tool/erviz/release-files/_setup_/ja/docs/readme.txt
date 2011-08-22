ソフトウェア名称:
    Erviz version 1.0.6

リリース日付:
    2010/07/27

概要:
    Ervizは、ER図を素早く描くための無償ツールです。WindowsやUnix/Linuxなどで動作します。
    
    Ervizでは、以下のような手順でER図を作成します。
    
      1. エンティティとリレーションシップに関するテキストファイルを作成する。
         テキストファイルは、簡単な言語で記述される。
    
      2. Ervizを使用して、テキストファイルをER図の画像ファイルに変換する。
         ER図の表記法や画像の形式は、変換時に指定する。

動作環境:
    Ervizは、以下のソフトウェアを必要とします。
    
      * Java Runtime Environment (JRE) 6
        http://java.com/ja/download/index.jsp
    
      * Graphviz - Graph Visualization Software (Version 2.26以降を推奨)
        http://www.graphviz.org/
    
    これらのソフトウェアがお使いの環境にインストールされていない場合は、
    上記のURLからダウンロードし、インストールしてください。
    (もしくは、APTやRPMなどのパッケージ管理システムを使用してください。)

インストール:
    以下の作業を実行してください。
      1. アーカイブを適当なディレクトリに展開してください。
      2. 「setup.cmd」または「setup.sh」を実行してください。(おそらく実行済だと思います。)

アンインストール:
    Ervizのディレクトリを削除してください。

ディレクトリ一覧:
    * bin       - Jarファイルとスクリプト
    * docs      - ドキュメント
    * html-docs - HTMLドキュメント
    * work      - 作業ディレクトリ

使用方法:
    「work」ディレクトリが作業ディレクトリとなります。
    基本的な使用方法については、「./work/NOTE.txt」を参照してください。
    
    必要に応じて、以下のドキュメントを参照してください。
      * HTMLドキュメント
        ./html-docs/index.html
      * Webページ
        http://www.ab.auone-net.jp/~simply/ja/works/erviz/about.html

ライセンス:
    Copyright (C) 2010 Simply Software
    
    このソフトウェアには、修正BSDライセンスが適用されます。
    詳細については、「./docs/license.txt」を参照してください

開発者:
    kono (Simply Software)

連絡先:
    URL:  http://www.ab.auone-net.jp/~simply/ja/
    Mail: simply@ac.auone-net.jp
    
    何かありましたらご連絡ください。
    特に以下のような内容についてお待ちしております。
      * このソフトウェアに関する改善依頼、感想、バグ報告
      * カスタム版または類似ツールの開発依頼  (有償)
      * 英文に関する間違いの指摘
