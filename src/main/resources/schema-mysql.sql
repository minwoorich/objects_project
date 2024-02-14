DROP TABLE IF EXISTS member;

CREATE TABLE member
(
    member_id  bigint auto_increment
        primary key,
    membership varchar(255),
    email      varchar(255),
    password   varchar(255),
    name       varchar(255),
    phone_no   varchar(255),
    is_alert   bit,
    is_agree   bit,
    created_at datetime(6),
    updated_at datetime(6),
    deleted_at datetime(6)
);

DROP TABLE IF EXISTS membership;

CREATE TABLE membership
(
    membership_id      bigint auto_increment
        primary key,

    member_id          varchar(255),
    cid                varchar(255),
    tid                varchar(255),
    sid                varchar(255),
    item_name          varchar(255),
    subs_order_no      varchar(255),
    status_code        varchar(255),
    payment_method     varchar(255),
    quantity           bigint,
    total_amount       bigint,
    discount_amount    bigint,
    tax_free_amount    bigint,
    card_issuer_name   varchar(255),
    card_purchase_name varchar(255),
    card_no            varchar(255),
    card_install_month BIGINT,
    created_at         datetime(6),
    updated_at         datetime(6),
    deleted_at         datetime(6)
);

DROP TABLE IF EXISTS address;

CREATE TABLE address
(
    address_id bigint auto_increment
        primary key,
    member_id  bigint,
    alias      varchar(255),
    name       varchar(255),
    phone_no   varchar(255),
    city       varchar(255),
    street     varchar(255),
    zipcode    varchar(255),
    detail     varchar(255),
    is_default bit,
    created_at datetime(6),
    updated_at datetime(6),
    deleted_at datetime(6)
);


DROP TABLE IF EXISTS category;

CREATE TABLE category
(
    category_id bigint auto_increment
        primary key,
    parent_id   bigint,
    level       bigint,
    name        varchar(255),
    created_at  datetime(6),
    updated_at  datetime(6),
    deleted_at  datetime(6)
);

DROP TABLE IF EXISTS product_image;

CREATE TABLE product_image
(
    product_image_id bigint auto_increment
        primary key,
    product_id       bigint,
    image_id         bigint,
    img_type         varchar(255),
    seq_no           bigint,
    created_at       datetime(6),
    updated_at       datetime(6),
    deleted_at       datetime(6)
);

DROP TABLE IF EXISTS image;

CREATE TABLE image
(
    image_id   bigint auto_increment
        primary key,
    url        varchar(255),
    created_at datetime(6),
    updated_at datetime(6),
    deleted_at datetime(6)
);

DROP TABLE IF EXISTS option_category;

CREATE TABLE option_category
(
    option_category_id bigint auto_increment
        primary key,

    name               varchar(255),
    created_at         datetime(6),
    updated_at         datetime(6),
    deleted_at         datetime(6)
);

DROP TABLE IF EXISTS options;

CREATE TABLE options
(
    option_id          bigint auto_increment
        primary key,

    option_category_id bigint,
    name               varchar(255),
    created_at         datetime(6),
    updated_at         datetime(6),
    deleted_at         datetime(6)
);

DROP TABLE IF EXISTS tag_category;
CREATE TABLE tag_category
(
    tag_category_id bigint auto_increment
        primary key,

    name            varchar(255),
    created_at      datetime(6),
    updated_at      datetime(6),
    deleted_at      datetime(6)
);

DROP TABLE IF EXISTS tag;

CREATE TABLE tag
(
    tag_id          bigint auto_increment
        primary key,
    tag_category_id bigint,
    name            varchar(255),
    created_at      datetime(6),
    updated_at      datetime(6),
    deleted_at      datetime(6)
);

DROP TABLE IF EXISTS prod_tag;

CREATE TABLE prod_tag
(
    prod_tag_id bigint auto_increment
        primary key,

    tag_id      bigint,
    product_id  bigint,
    created_at  datetime(6),
    updated_at  datetime(6),
    deleted_at  datetime(6)
);


DROP TABLE IF EXISTS status_code;

CREATE TABLE status_code
(
    status_code_id bigint auto_increment
        primary key,

    type           varchar(255),
    code           varchar(255),
    name           varchar(255),
    created_at     datetime(6),
    updated_at     datetime(6),
    deleted_at     datetime(6)
);

DROP TABLE IF EXISTS orders;

CREATE TABLE orders
(
    order_id       bigint auto_increment
        primary key,

    member_id      bigint,
    address_id     bigint,
    order_name     varchar(255),
    order_no       varchar(255),
    total_discount bigint,
    total_price    bigint,
    real_price     bigint,
    tid            varchar(255),
    created_at     datetime(6),
    updated_at     datetime(6),
    deleted_at     datetime(6)
);

DROP TABLE IF EXISTS order_detail;

CREATE TABLE order_detail
(
    order_detail_id bigint auto_increment
        primary key,
CREATE TABLE order_detail (
                              order_detail_id	bigint auto_increment
                                  primary key,

    order_id        bigint,
    product_id      bigint,
    order_no        varchar(255),
    member_coupon_id  bigint,
    reward_type     varchar(255),
    quantity        bigint,
    price           bigint,
    seller_id       bigint,
    status_code     varchar(255),
    tid             varchar(255),
    delivered_date  datetime(6),
    reason          text,
    cancelled_at    datetime(6),
    created_at      datetime(6),
    updated_at      datetime(6),
    deleted_at      datetime(6)
);

DROP TABLE IF EXISTS payment;

CREATE TABLE payment
(
    payment_id         bigint auto_increment
        primary key,

                         order_id	bigint	,
                         order_no	varchar(255)	,
                         payment_method	varchar(255)	,
                         tid	varchar(255)	,
                         card_issuer_name	varchar(255)	,
                         card_purchase_name	varchar(255)	,
                         card_no	varchar(255)	,
                         card_install_month	BIGINT	,
                         total_amount BIGINT,
                         discount_amount BIGINT,
                         tax_free_amount BIGINT,
                         approved_at	datetime(6)	,
                         pg_token       varchar(255),
                         deleted_at	datetime(6)	,
                         updated_at	datetime(6)	,
                         created_at	datetime(6)
);

DROP TABLE IF EXISTS review;

CREATE TABLE review (
                        review_id	bigint auto_increment
                            primary key,

                        member_id	bigint	,
                        product_id	bigint	,
                        order_detail_id bigint,
                        content	text	,
                        rating	bigint	,
                        created_at	datetime(6)	,
                        updated_at	datetime(6)	,
                        deleted_at	datetime(6)
);

DROP TABLE IF EXISTS review_image;

CREATE TABLE review_image
(
    review_image_id bigint auto_increment
        primary key,

    review_id       bigint,
    image_id        bigint,
    seq_no          bigint,
    created_at      datetime(6),
    updated_at      datetime(6),
    deleted_at      datetime(6)
);

DROP TABLE IF EXISTS review_likes;

CREATE TABLE review_likes
(
    review_likes_id bigint auto_increment
        primary key,
    review_id       bigint,
    member_id       bigint,
    likes           bigint,
    created_at      datetime(6),
    updated_at      datetime(6),
    deleted_at      datetime(6)
);

DROP TABLE IF EXISTS review_survey;

CREATE TABLE review_survey
(
    review_survey_id          bigint auto_increment
        primary key,

    review_id                 bigint,
    review_survey_category_id bigint,
    survey_content_id         bigint,
    created_at                datetime(6),
    updated_at                datetime(6),
    deleted_at                datetime(6)
);

DROP TABLE IF EXISTS review_survey_category;

CREATE TABLE review_survey_category
(
    review_survey_category_id bigint auto_increment
        primary key,

    product_id                bigint,
    name                      varchar(255),
    created_at                datetime(6),
    updated_at                datetime(6),
    deleted_at                datetime(6)
);

DROP TABLE IF EXISTS survey_content;

CREATE TABLE survey_content
(
    survey_content_id         bigint auto_increment
        primary key,

    review_survey_category_id bigint,
    seq_no                    bigint,
    content                   varchar(255),
    created_at                datetime(6),
    updated_at                datetime(6),
    deleted_at                datetime(6)
);

DROP TABLE IF EXISTS coupon;

CREATE TABLE coupon
(
    coupon_id     bigint auto_increment
        primary key,

    product_id    bigint,
    name          varchar(255),
    price         varchar(255),
    count         bigint,
    minimum_price bigint,
    start_date    datetime(6),
    end_date      datetime(6),
    created_at    datetime(6),
    updated_at    datetime(6),
    deleted_at    datetime(6)
);


DROP TABLE IF EXISTS member_coupon;

CREATE TABLE member_coupon
(
    member_coupon_id bigint auto_increment primary key,
    member_id        bigint,
    coupon_id        bigint,
    is_used          bit,
    used_date        datetime(6),
    end_date         datetime(6),
    created_at       datetime(6),
    updated_at       datetime(6),
    deleted_at       datetime(6)
);


DROP TABLE IF EXISTS cart;

CREATE TABLE cart
(
    cart_id    bigint auto_increment primary key,
    member_id  bigint,
    product_id bigint,
    is_subs    bit,
    quantity   bigint
);

DROP TABLE IF EXISTS prod_option;

CREATE TABLE prod_option
(
    prod_option_id bigint auto_increment primary key,
    product_id     bigint,
    option_id      bigint,
    created_at     datetime(6),
    updated_at     datetime(6),
    deleted_at     datetime(6)
);

DROP TABLE IF EXISTS product;

CREATE TABLE product
(
    product_id    bigint auto_increment primary key,
    category_id   bigint,
    is_own        bit,
    name          varchar(255),
    price         bigint,
    is_subs       bit,
    stock         bigint,
    thumb_img     varchar(255),
    discount_rate bigint,
    product_no    varchar(255),
    created_at    datetime(6),
    updated_at    datetime(6),
    deleted_at    datetime(6)
);