package ru.kabylin.androidarchexample.client

interface HttpListener {
    /**
     * Пример использования:
     *
     * override fun onHttpResponse(requestCode: Int, response: HttpResponse) {
     *     if (requestCode != RegistrationPresenter.REQUEST_SERVICE_REQUEST_REGISTRATION)
     *         return
     *
     *     runOnUiThread {
     *         when (response.statusCode) {
     *             HttpStatusCode.OK ->
     *                 alert("OK", "Success")
     *
     *             HttpStatusCode.BAD_REQUEST -> {
     *                 alert(
     *                     title = "Error!",
     *                     message = """
     *                         Oh no! bad request!
     *                         HEADERS:
     *                         ${response.headers}
     *                         BODY:
     *                         ${String(response.body!!)}""".trimIndent()
     *                 )
     *             }
     *         }
     *     }
     * }
     */
    fun onHttpResponse(requestCode: Int, response: HttpResponse)
}
