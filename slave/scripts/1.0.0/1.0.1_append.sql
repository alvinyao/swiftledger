-- 2019-04-18 modify by liuyu
alter table policy add column `verifyNum` int(8) DEFAULT NULL COMMENT 'verifyNum';
alter table policy add column `mustRsIds` VARCHAR(1024) DEFAULT NULL COMMENT 'mustRsIds';
