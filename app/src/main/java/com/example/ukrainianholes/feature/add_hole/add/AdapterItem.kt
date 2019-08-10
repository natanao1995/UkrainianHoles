package com.example.ukrainianholes.feature.add_hole.add

open class AdapterItem(val type: ItemType)

enum class ItemType(val id: Int) {
    PHOTO_ITEM(0),
    ADD_ITEM(1)
}

class AddItem : AdapterItem(ItemType.ADD_ITEM)

class PhotoItem(val photoId: Long) : AdapterItem(ItemType.PHOTO_ITEM)