package com.example.food_delivery

public class FoodItem(name: String, description: String, price: Double, image: Int){

    private var name = name
    private var description = description
    private var price = price
    private var image = image

    fun getName(): String{
        return this.name
    }

    fun getDescription(): String{
        return this.description
    }

    fun getPrice(): Double{
        return this.price
    }

    fun getImage(): Int{
        return this.image
    }

}