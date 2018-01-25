package ru.kabylin.androidarchexample

data class Book(val id: Int, val name: String, val author: String)

enum class QueryQualifier {
    EQ,
    GT,
    LT,
    GTE,
    LTE,
    AND,
    OR,
    NOT
}

data class QueryPair<out A, out B>(val a: A, val b: B?, val qualifier: QueryQualifier)
//class AndQueryArg<out A: Q<*>, out B: Q<*>>(val q1: A, q2: B): Qu

data class Q<out A, out B>(val pair: QueryPair<A, B>)

class Repository {
    private val books = ArrayList<Book>()

    init {
        books.add(Book(id = 10, name = "Test", author = "Test"))
    }

    fun get(query: Q<*, *>): Book {
        return books.first {
            var get = true
            val pair = query.pair

            when (query.pair.qualifier) {
                QueryQualifier.EQ -> {
                    get = get && when (query.pair.a) {
                        "id" -> it.id == pair.b
                        "name" -> it.name == pair.b
                        "author" -> it.author == pair.b
                        else -> false
                    }
                }
                QueryQualifier.GT -> {
                    get = get && when (query.pair.a) {
                        "id" -> it.id > pair.b as Int
                        else -> false
                    }
                }
                QueryQualifier.GTE -> {
                    get = get && when (query.pair.a) {
                        "id" -> it.id >= pair.b as Int
                        else -> false
                    }
                }
                else -> {
                }
            }

            get
        }
    }
}

fun ex() {
    val repository = Repository()
//    val query = Query<Unit>().get<Book>()

//    val query = repository
//        .where(Q("id" eq 10) and Q("author" eq "Test"))
//        .get<Book>()

//    val q1 : Q<Int> = Q("id" eq 10)
//    val q2 : Q<String> = Q("author" eq "Test")
//    val andQ : AndQuery<Q<Int>, Q<String>> = q1 and q2

//    val query = (Q("id" eq 10) and Q("author" eq "Test")) or Q("id" eq 21)
//    val q2 = not(query)
    val book: Book = repository.get(Q("author" eq "Andrey"))
}

fun not(query: Q<*, *>) =
    Q(QueryPair(query, null, QueryQualifier.NOT))

//
private infix fun <A : Q<*, *>, B : Q<*, *>> A.and(q: B) = Q(QueryPair(this, q, QueryQualifier.AND))

private infix fun <A : Q<*, *>, B : Q<*, *>> A.or(q: B) = Q(QueryPair(this, q, QueryQualifier.OR))

private infix fun <A, B> A.eq(value: B) = QueryPair(this, value, QueryQualifier.EQ)

private infix fun <A, B> A.gt(value: B) = QueryPair(this, value, QueryQualifier.GT)

private infix fun <A, B> A.lt(value: B) = QueryPair(this, value, QueryQualifier.LT)

private infix fun <A, B> A.gte(value: B) = QueryPair(this, value, QueryQualifier.GTE)

private infix fun <A, B> A.lte(value: B) = QueryPair(this, value, QueryQualifier.LTE)
