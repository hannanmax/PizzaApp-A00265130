package com.hannanmax.pizzaapp_a00265130.Data

data class OrdersItemList(var order_date: String ?= null,
                          var cartTotal: Float ?= null,
                          var delivery: Float ?= null,
                          var tax: Float ?= null,
                          var cartTotalWithTax: Float ?= null,
                          var phoneno: String ?= null,
                          var houseno: String ?= null,
                          var streetname: String ?= null,
                          var city: String ?= null,
                          var province: String ?= null,
                          var postalcode: String ?= null)
