package com.example.food_delivery

public class Cart {

    private var items = arrayListOf<FoodItem>()

    fun getCartList(): ArrayList<FoodItem>{
        return this.items
    }

    fun amountInCart(): Int{
        return this.items.count()
    }

    fun totalPrice(): Double{
        var total = 0.0;
        for(i in 0..amountInCart()){
            total += items[i].getPrice()
        }

        return total
    }

    fun addToCart(item: FoodItem, n: Int){
        for(i in 0..n){
            this.items.add(item)
        }
    }

}