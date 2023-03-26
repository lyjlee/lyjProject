set timezone = 'Asia/Seoul';

INSERT INTO mydb.account (user_id, user_password, user_name, user_email)
VALUES ('lyj6752', '123123123', 'youngjae', 'lyj6752@naver.com');

INSERT INTO mydb.board (board_title, board_des, user_name, board_submittedAt)
VALUES ('1번 글','반갑다 새로운 글이다1','lyj',current_timestamp),
       ('2번 글','반갑다 새로운 글이다2','hgd',current_timestamp),
       ('3번 글','반갑다 새로운 글이다3','kcs',current_timestamp),
       ('4번 글','반갑다 새로운 글이다3','kcs',current_timestamp),
       ('5번 글','반갑다 새로운 글이다3','kcs',current_timestamp),
       ('6번 글','반갑다 새로운 글이다3','kcs',current_timestamp),
       ('7번 글','반갑다 새로운 글이다3','kcs',current_timestamp),
       ('8번 글','반갑다 새로운 글이다3','kcs',current_timestamp),
       ('9번 글','반갑다 새로운 글이다3','kcs',current_timestamp),
       ('10번 글','반갑다 새로운 글이다3','kcs',current_timestamp),
       ('11번 글','반갑다 새로운 글이다3','kcs',current_timestamp),
       ('12번 글','반갑다 새로운 글이다3','kcs',current_timestamp),
       ('13번 글','반갑다 새로운 글이다3','kcs',current_timestamp),
       ('14번 글','반갑다 새로운 글이다3','kcs',current_timestamp),
       ('15번 글','반갑다 새로운 글이다3','kcs',current_timestamp),
       ('16분 글','반갑다 새로운 글이다3','kcs',current_timestamp),
       ('17번 글','반갑다 새로운 글이다3','kcs',current_timestamp),
       ('18번 글','반갑다 새로운 글이다3','kcs',current_timestamp),
       ('19번 글','반갑다 새로운 글이다3','kcs',current_timestamp),
       ('20번 글','반갑다 새로운 글이다3','kcs',current_timestamp),
       ('21번 글','반갑다 새로운 글이다3','kcs',current_timestamp),
       ('22번 글','반갑다 새로운 글이다4','kys',current_timestamp);

INSERT INTO mydb.reply (board_no, user_name, reply_content, reply_submittedAt)
VALUES ('1','홍길동','반갑다 새로운 댓글이다1',current_timestamp),
       ('1','장길산','반갑다 새로운 댓글이다2',current_timestamp);

INSERT INTO mydb.reply (board_no, user_name, reply_content, reply_submittedAt, reply_level, reply_parent_no)
VALUES ('1','홍길동1','반갑다 새로운 댓글이다5',current_timestamp,2,1),
     ('1','홍길동2','반갑다 새로운 댓글이다6',current_timestamp,3,3),
     ('1','홍길동3','반갑다 새로운 댓글이다7',current_timestamp,4,4),
     ('1','홍길동4','반갑다 새로운 댓글이다8',current_timestamp,2,2),
     ('1','홍길동5','반갑다 새로운 댓글이다9',current_timestamp,1,0),
     ('1','홍길동6','반갑다 새로운 댓글이다10',current_timestamp,2,7);

INSERT INTO mydb.reply (board_no, user_name, reply_content, reply_submittedAt)
VALUES ('1','이영재','반갑다 새로운 댓글이다11',current_timestamp),
       ('1','이영재','반갑다 새로운 댓글이다12',current_timestamp),
       ('1','이영재','반갑다 새로운 댓글이다12',current_timestamp),
       ('1','이영재','반갑다 새로운 댓글이다12',current_timestamp),
       ('1','이영재','반갑다 새로운 댓글이다12',current_timestamp),
       ('1','이영재','반갑다 새로운 댓글이다12',current_timestamp),
       ('1','이영재','반갑다 새로운 댓글이다12',current_timestamp),
       ('1','이영재','반갑다 새로운 댓글이다12',current_timestamp),
       ('1','이영재','반갑다 새로운 댓글이다12',current_timestamp),
       ('1','이영재','반갑다 새로운 댓글이다12',current_timestamp),
       ('1','이영재','반갑다 새로운 댓글이다12',current_timestamp);

INSERT INTO mydb.movie_db (movie_subject, movie_img, movie_genre)
VALUES ('inception', '/images/poster1.jpg', 'sf'),
       ('dark knight', '/images/poster2.jpg', 'action'),
       ('memento', '/images/poster3.jpg','crime');

INSERT INTO mydb.movie_eval (movie_subject, movie_score, movie_comment, movie_user_id, comment_submittedAt)
VALUES ('inception', 3, '좋아요', 'lyj6752',current_timestamp),
       ('inception', 1, '별로예요', 'asd4a8s',current_timestamp),
       ('dark knight', 4, '재밌어요', 'd156a',current_timestamp),
       ('dark knight', 5, '최고예요', 'as48as',current_timestamp),
       ('memento', 3, '그냥그래요', 'ads1asd2',current_timestamp),
       ('memento', 2, '난해해요', 'sd8sad8',current_timestamp);

