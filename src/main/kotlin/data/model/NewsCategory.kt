
package data.model

enum class NewsCategory {
    Politics,
    Business,
    Society,
    Crime,
    Technology,
    Science,
    Sports,
    Culture,
    Showbiz,
    Automotive,
    Health,
    Travel,
    Lifestyle,
    Environment,
    Education,
    Regional,
    Breaking,
    Opinion,
    Finance,
    Gaming,
    History,
    Humor
}

fun String.toNewsCategory(): NewsCategory?
{
    return try {
        NewsCategory.valueOf(this.capitalize())
    }catch (e: Exception){
        null
    }
}