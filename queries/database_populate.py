import random
from datetime import datetime, timedelta
with open("orders.csv", "w") as order_writer:
    with open("order_details.csv", "w") as detail_writer:
        with open("order_modifiers.csv", "w") as modifier_writer:
            order_writer.write("emlpoyee_id,created_at,total_tax,total_final\n")
            detail_writer.write("order_id,product_id,sold_price,snapshot_name\n")
            modifier_writer.write("order_detail_id,modifier_option_id,price_charged,snapshot_name\n")
            big_days = [random.randint(0,364), random.randint(0,364), random.randint(0, 364)]
            order_id = 1
            detail_id = 1
            modifier_id = 1
            new_year = datetime(2026, 1, 1)
            modifier_prices = [0, 0.5, 0.75, 0.75, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.75, 0, 0.5, 0.5, 0.5]
            modifiers = ["", "Pearl (Boba)", "White Pearl (Crystal)", "Milk Foam", "Pudding", "Herbal Jelly", "Coconut Jelly", "Basil Seeds",
                         "Ai-Yu Jelly", "Oreo Crumbs", "100% Sugar", "70% Sugar", "50% Sugar", "30% Sugar", "0% Sugar", "Regular Ice",
                         "Less Ice", "No Ice", "Extra Ice", "Medium", "Large", "Whole Milk", "Almond Milk", "Oat Milk", "Soy Milk"]
            menu_prices = [0, 5.75, 5.75, 5.75, 5.75, 7, 5.7, 4.9, 5.7, 6.5, 5.4, 5.7, 4.9, 5.4, 5.4, 5.95, 5.4, 5.4, 5.95, 4.5, 4.5, 4.5, 4.5, 5.95, 5.4, 6.25, 6.75, 6.75]
            menu = ["", "Milk Foam Green Tea", "Milk Foam Black Tea", "Milk Foam Earl Grey Tea", "Milk Foam Wintermelon", "Creme Brulee Brown Sugar Milk Tea",
                    "Pearl Milk Tea", "Black Milk Tea", "Taro Milk Tea", "Earl Grey Milk Tea w/ 3J", "Brown Sugar Milk Tea", "Strawberry Milk Tea",
                    "Oolong Milk Tea", "Mango Green Tea", "Passionfruit Green Tea", "Lemon Wintermelon w/ Basil Seeds", "Lychee Oolong Tea",
                    "Hibiscus Green Tea", "Lemon Ai-Yu w/ White Pearl", "Jasmine Green Tea", "Black Tea", "Oolong Tea", "Earl Grey Tea", 
                    "Milk Foam Black Coffee", "Coffee Milk Tea", "Taro Slush", "Matcha Milk Slush", "Peach Slush"]
            for day in range(365):
                num_orders = 200
                if day in big_days:
                    num_orders = 450
                for order in range(num_orders):
                    employee_id = random.randint(1, 5)
                    date_time = new_year + timedelta(day, random.randint(0, 32400), 0, 0, 0, 9)
                    numItems = random.randint(1, 4)
                    order_price = 0
                    items = []
                    for i in range(numItems):
                        product_id = random.randint(1, 27)
                        item_price = menu_prices[product_id]
                        items.append(product_id)
                        snapshot = menu[product_id]
                        numToppings = random.randint(0, 2)
                        ice = random.randint(15, 18)
                        sugar = random.randint(10, 14)
                        size = random.randint(19, 20)
                        milk = random.randint(21, 24)
                        snapshot += f" + {modifiers[ice]} + {modifiers[sugar]} + {modifiers[size]} + {modifiers[milk]}"
                        item_price += modifier_prices[ice] + modifier_prices[sugar] + modifier_prices[size] + modifier_prices[milk]
                        modifier_writer.write(",".join([str(detail_id), str(ice), str(modifier_prices[ice]), modifiers[ice]]))
                        modifier_writer.write("\n")
                        modifier_id += 1
                        modifier_writer.write(",".join([str(detail_id), str(sugar), str(modifier_prices[sugar]), modifiers[sugar]]))
                        modifier_writer.write("\n")
                        modifier_id += 1
                        modifier_writer.write(",".join([str(detail_id), str(size), str(modifier_prices[size]), modifiers[size]]))
                        modifier_writer.write("\n")
                        modifier_id += 1
                        modifier_writer.write(",".join([str(detail_id), str(milk), str(modifier_prices[milk]), modifiers[milk]]))
                        modifier_writer.write("\n")
                        modifier_id += 1
                        for j in range(numToppings):
                            topping_id = random.randint(1, 9)
                            item_price += modifier_prices[topping_id]
                            snapshot += f" + {modifiers[topping_id]}"
                            modifier_writer.write(",".join([str(detail_id), str(topping_id), str(modifier_prices[topping_id]), modifiers[topping_id]]))
                            modifier_writer.write("\n")
                            modifier_id += 1
                        order_price += item_price
                        detail_writer.write(",".join([str(order_id), str(product_id), str(item_price), snapshot]))
                        detail_writer.write("\n")
                        detail_id += 1
                    # total_tax = sum(menu_prices[item] for item in items)
                    order_writer.write(",".join([str(employee_id), str(date_time), str(order_price), f"{order_price*1.0825:.2f}"]))
                    order_writer.write("\n")
                    order_id += 1