package net.deechael.esjzone.client.types

enum class Types(val index: Int) {

    /**
     * 全部
     */
    ALL(0),

    /**
     * 日轻
     */
    JAPANESE(1),

    /**
     * 原创
     */
    ORIGINAL(2),

    /**
     * 韩轻
     */
    KOREAN(3)

}

enum class Sorts(val index: Int) {

    /**
     * 最新更新
     */
    RECENTLY_UPDATED(1),

    /**
     * 最新创建
     */
    RECENTLY_CREATED(2),

    /**
     * 最高评分
     */
    HIGHEST_RATING(3),

    /**
     * 最多观看
     */
    MOST_VIEWS(4),

    /**
     * 最多文章
     */
    MOST_CHAPTERS(5),

    /**
     * 最多讨论
     */
    MOST_COMMENTS(6),

    /**
     * 最多收藏
     */
    MOST_FAVORITES(7),

    /**
     * 最多字数
     */
    MOST_WORDS(8)

}