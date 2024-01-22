package lunamall.example.test18.model

data class Order (
    var order_time: String?,
    var userId: String?,
    var phone: String,
    var address: String,
    var inquire: String,
    var order_menu: String?,
    var order_price: Int?,
    var total: Int?,
    var deliver: String?
)