package com.muliamaulana.github.utils

import com.muliamaulana.github.data.source.remote.response.ItemListUser

/**
 * Created by muliamaulana on 15/02/25.
 */
object DataDummy {

    fun generateDummyResponse(): MutableList<ItemListUser> {
        val items = mutableListOf<ItemListUser>()
        for (i in 1 until 30) {
            val user = ItemListUser(
                login = "username$i",
            )
            items.add(user)
        }

        return items
    }
}