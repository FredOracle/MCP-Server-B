create table china_area
(
    id          int auto_increment comment 'ID'
        primary key,
    pid         int          null comment '父id',
    shortname   varchar(100) null comment '简称',
    name        varchar(100) null comment '名称',
    mergename   varchar(255) null comment '全称',
    level       tinyint      null comment '层级 0 1 2 省市区县',
    pinyin      varchar(100) null comment '拼音',
    area_code   varchar(100) null comment '电话长途区号',
    zip_code    varchar(100) null comment '邮政编码',
    initial     varchar(50)  null comment '首字母',
    lng         varchar(100) null comment '经度',
    lat         varchar(100) null comment '纬度',
    initials    varchar(50)  null comment '拼音缩写',
    suffix      varchar(50)  null comment '后辍',
    region_code varchar(50)  null comment '行政区划分代码'
)
    comment '地区表' charset = utf8
                     row_format = DYNAMIC;

create index pid
    on china_area (pid);



create table district
(
    id         int          null,
    parent_id  varchar(50)  null,
    name       varchar(50)  null,
    code       varchar(50)  null,
    initial    varchar(50)  null comment '首字母',
    initials   varchar(500) null comment '缩写',
    suffix     varchar(50)  null,
    pinyin     varchar(50)  null comment '拼音全称',
    level      int          null,
    zip        varchar(50)  null,
    short_name varchar(50)  null,
    merge_name varchar(255) null,
    latitude   double       null,
    longitude  double       null
);

