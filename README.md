# DZT-Chat
DZT实时加密聊天

简单的基于Java的 __端对端加密__ 即时通讯工具。
服务端仅负责帮助位于内网的客户端进行消息转发，全程不干预通讯和保存信息。

用户通过输入RSA私钥来获取用户名和进行连接

* 客户端带GUI吗？
    - 带一个比较简单的GUI，带有操作提示。
* 怎样注册账号？
    - 不需要注册。项目中包含了RSA秘钥生成器，生成以后填入软件页面就可以了。用户名会根据你的秘钥使用哈希算法生成。
* 秘钥怎么找回？
    - 无法找回。所以记得保存好。
* DZT是什么的缩写？
    - 记不得了。可能是Distribute之类单词的缩写。
-----

Simple Java-based __end-to-end encrypted__ instant messaging tool .
The server is only responsible for helping the client located on the intranet to forward messages, and does not interfere with communication or save messages throughout.

The user enters the RSA private key to obtain the user name and make the connection

* Does the client come with a GUI?
    - No. It comes with a simple GUI with operation tips.
* How do I register my account?
    - No registration is required. The project includes an RSA key generator, just fill in the client gui after you generate it. The user name will be generated according to your secret key using hash algorithm.
* How to recover the RSA key?
    - There is no way to recover it. So remember to save it.
* What does __DZT__ stand for?
    - I can't remember. It might be an abbreviation for something like Distribute.
