-- 2019-05-05 liuyu
ALTER TABLE `vote_request_record`
CHANGE COLUMN `tx_data` `tx_data` MEDIUMTEXT NOT NULL COMMENT 'the tx data' ;
