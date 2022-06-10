file-api port : 10834

----------------------------------------------------------------

[GET] download one file
http://116.43.4.165:10833/board/posts/{post_id}/{file_id}

[GET] get file info by post id
http://116.43.4.165:10833/board/posts/{post_id}

[GET] download profile by user id
http://116.43.4.165:10833/users/{user_id}

----------------------------------------------------------------

[POST] file upload
http://116.43.4.165:10834/board/posts/{post_id}
key: ufile | value: files

[POST] profile img upload 
http://116.43.4.165:10834/users/{user_id}
key: profile | value: file

----------------------------------------------------------------

[PUT] file modify
http://116.43.4.165:10834/board/posts/{post_id}
key: ufile | value: files

[PUT] profile img upload 
http://116.43.4.165:10834/users/{user_id}
key: profile | value: file

----------------------------------------------------------------

[DELETE] delete post by post id
http://116.43.4.165:10833/users/{user_id}

[DELETE] delete profile by user id
http://116.43.4.165:10833/users/{user_id}

