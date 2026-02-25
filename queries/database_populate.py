import random
from datetime import datetime, timedelta

TAX_RATE = 0.0825

MODIFIER_PRICES = [
    0, 0.5, 0.75, 0.75, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5,
    0, 0, 0, 0, 0,
    0, 0, 0, 0,
    0, 0.75,
    0, 0.5, 0.5, 0.5,
]

MODIFIERS = [
    "",
    "Pearl (Boba)", "White Pearl (Crystal)", "Milk Foam", "Pudding", "Herbal Jelly", "Coconut Jelly", "Basil Seeds", "Ai-Yu Jelly", "Oreo Crumbs",
    "100% Sugar", "70% Sugar", "50% Sugar", "30% Sugar", "0% Sugar",
    "Regular Ice", "Less Ice", "No Ice", "Extra Ice",
    "Medium", "Large",
    "Whole Milk", "Almond Milk", "Oat Milk", "Soy Milk",
]

MENU_PRICES = [
    0, 5.75, 5.75, 5.75, 5.75, 7.00,
    5.70, 4.90, 5.70, 6.50, 5.40, 5.70, 4.90,
    5.40, 5.40, 5.95, 5.40, 5.40, 5.95,
    4.50, 4.50, 4.50, 4.50,
    5.95, 5.40,
    6.25, 6.75, 6.75,
]

MENU = [
    "",
    "Milk Foam Green Tea", "Milk Foam Black Tea", "Milk Foam Earl Grey Tea", "Milk Foam Wintermelon", "Creme Brulee Brown Sugar Milk Tea",
    "Pearl Milk Tea", "Black Milk Tea", "Taro Milk Tea", "Earl Grey Milk Tea w/ 3J", "Brown Sugar Milk Tea", "Strawberry Milk Tea", "Oolong Milk Tea",
    "Mango Green Tea", "Passionfruit Green Tea", "Lemon Wintermelon w/ Basil Seeds", "Lychee Oolong Tea", "Hibiscus Green Tea", "Lemon Ai-Yu w/ White Pearl",
    "Jasmine Green Tea", "Black Tea", "Oolong Tea", "Earl Grey Tea",
    "Milk Foam Black Coffee", "Coffee Milk Tea",
    "Taro Slush", "Matcha Milk Slush", "Peach Slush",
]

PRODUCT_GROUPS = {
    "milk_foam": list(range(1, 6)),
    "milk_tea": list(range(6, 13)),
    "creative_mix": list(range(13, 19)),
    "brewed_tea": list(range(19, 23)),
    "coffee": list(range(23, 25)),
    "slush": list(range(25, 28)),
}

WEEKDAY_VOLUME = [0.92, 0.88, 0.95, 1.00, 1.12, 1.28, 1.18]


def weighted_choice(options, weights):
    return random.choices(options, weights=weights, k=1)[0]


def seasonal_item_weights(month):
    if month in (12, 1, 2):
        return {
            "milk_foam": 1.25,
            "milk_tea": 1.20,
            "creative_mix": 0.85,
            "brewed_tea": 1.10,
            "coffee": 1.15,
            "slush": 0.75,
        }
    if month in (6, 7, 8):
        return {
            "milk_foam": 0.95,
            "milk_tea": 1.00,
            "creative_mix": 1.30,
            "brewed_tea": 0.90,
            "coffee": 0.80,
            "slush": 1.45,
        }
    return {
        "milk_foam": 1.00,
        "milk_tea": 1.10,
        "creative_mix": 1.05,
        "brewed_tea": 1.00,
        "coffee": 0.95,
        "slush": 1.00,
    }


def choose_order_time(day_start):
    hour = weighted_choice(
        [9, 10, 11, 12, 13, 14, 15, 16, 17],
        [3, 7, 10, 12, 10, 11, 14, 15, 8],
    )
    minute = random.randint(0, 59)
    second = random.randint(0, 59)
    return day_start + timedelta(hours=hour, minutes=minute, seconds=second)


def choose_employee_id(order_time):
    hour = order_time.hour
    if hour <= 11:
        return weighted_choice([1, 2, 3, 4, 5, 6], [3, 2, 2, 1, 1, 1])
    if hour <= 14:
        return weighted_choice([1, 2, 3, 4, 5, 6], [1, 2, 2, 3, 2, 2])
    return weighted_choice([1, 2, 3, 4, 5, 6], [1, 2, 2, 2, 3, 3])


def choose_product(order_time):
    season_weights = seasonal_item_weights(order_time.month)
    if order_time.hour <= 11:
        daypart_boost = {
            "milk_foam": 1.00,
            "milk_tea": 1.00,
            "creative_mix": 0.90,
            "brewed_tea": 1.10,
            "coffee": 1.50,
            "slush": 0.70,
        }
    elif order_time.hour <= 14:
        daypart_boost = {
            "milk_foam": 1.00,
            "milk_tea": 1.05,
            "creative_mix": 1.05,
            "brewed_tea": 1.00,
            "coffee": 0.95,
            "slush": 1.05,
        }
    else:
        daypart_boost = {
            "milk_foam": 0.95,
            "milk_tea": 1.00,
            "creative_mix": 1.20,
            "brewed_tea": 0.90,
            "coffee": 0.75,
            "slush": 1.25,
        }

    categories = list(PRODUCT_GROUPS.keys())
    category_weights = [season_weights[c] * daypart_boost[c] for c in categories]
    chosen_category = weighted_choice(categories, category_weights)
    return random.choice(PRODUCT_GROUPS[chosen_category]), chosen_category


def choose_base_modifiers(category):
    sugar = weighted_choice([10, 11, 12, 13, 14], [28, 24, 26, 14, 8])

    if category == "slush":
        ice = weighted_choice([16, 17, 18], [45, 35, 20])
    elif category in ("coffee", "brewed_tea"):
        ice = weighted_choice([15, 16, 17, 18], [30, 40, 20, 10])
    else:
        ice = weighted_choice([15, 16, 17, 18], [55, 25, 12, 8])

    size = weighted_choice([19, 20], [62, 38])

    milk = None
    if category in ("milk_tea", "milk_foam", "coffee", "slush"):
        milk = weighted_choice([21, 22, 23, 24], [70, 10, 12, 8])

    return sugar, ice, size, milk


def choose_toppings(product_id, category):
    if product_id == 6:
        return []
    if product_id == 9:
        return []
    if product_id in (15, 18):
        return []

    if category in ("creative_mix", "slush"):
        count = weighted_choice([0, 1, 2], [40, 45, 15])
    elif category == "brewed_tea":
        count = weighted_choice([0, 1, 2], [55, 35, 10])
    else:
        count = weighted_choice([0, 1, 2], [48, 40, 12])

    if count == 0:
        return []

    return random.sample(range(1, 10), k=count)


def build_snapshot(base_name, modifier_ids):
    if not modifier_ids:
        return base_name
    parts = [MODIFIERS[mid] for mid in modifier_ids]
    return f"{base_name} + " + " + ".join(parts)


def daily_order_count(current_day, special_peak_days):
    day_of_week = current_day.weekday()
    month = current_day.month

    base = random.randint(120, 165)
    volume = base * WEEKDAY_VOLUME[day_of_week]

    if month in (6, 7, 8):
        volume *= 1.12
    elif month in (11, 12):
        volume *= 1.08
    elif month == 2:
        volume *= 0.93

    if current_day.timetuple().tm_yday in special_peak_days:
        volume *= random.uniform(1.6, 2.0)

    noise = random.uniform(0.90, 1.15)
    return max(75, int(volume * noise))


with open("orders.csv", "w") as order_writer:
    with open("order_details.csv", "w") as detail_writer:
        with open("order_modifiers.csv", "w") as modifier_writer:
            order_writer.write("employee_id,created_at,total_tax,total_final\n")
            detail_writer.write("order_id,product_id,sold_price,snapshot_name\n")
            modifier_writer.write("order_detail_id,modifier_option_id,price_charged,snapshot_name\n")

            special_peak_days = set(random.sample(range(1, 366), k=12))
            order_id = 1
            detail_id = 1
            start_date = datetime(2025, 1, 1)

            for day_offset in range(365):
                current_day = start_date + timedelta(days=day_offset)
                num_orders = daily_order_count(current_day, special_peak_days)

                for _ in range(num_orders):
                    order_time = choose_order_time(current_day)
                    employee_id = choose_employee_id(order_time)
                    item_count = weighted_choice([1, 2, 3, 4], [53, 30, 12, 5])

                    subtotal = 0.0
                    for _ in range(item_count):
                        product_id, category = choose_product(order_time)
                        base_price = MENU_PRICES[product_id]

                        sugar_id, ice_id, size_id, milk_id = choose_base_modifiers(category)
                        topping_ids = choose_toppings(product_id, category)

                        modifier_ids = [ice_id, sugar_id, size_id]
                        if milk_id is not None:
                            modifier_ids.append(milk_id)
                        modifier_ids.extend(topping_ids)

                        item_price = base_price + sum(MODIFIER_PRICES[mid] for mid in modifier_ids)
                        snapshot = build_snapshot(MENU[product_id], modifier_ids)

                        for mod_id in modifier_ids:
                            modifier_writer.write(
                                ",".join(
                                    [
                                        str(detail_id),
                                        str(mod_id),
                                        f"{MODIFIER_PRICES[mod_id]:.2f}",
                                        MODIFIERS[mod_id],
                                    ]
                                )
                            )
                            modifier_writer.write("\n")

                        detail_writer.write(
                            ",".join(
                                [
                                    str(order_id),
                                    str(product_id),
                                    f"{item_price:.2f}",
                                    snapshot,
                                ]
                            )
                        )
                        detail_writer.write("\n")

                        subtotal += item_price
                        detail_id += 1

                    tax = subtotal * TAX_RATE
                    total = subtotal + tax
                    order_writer.write(
                        ",".join(
                            [
                                str(employee_id),
                                str(order_time),
                                f"{tax:.2f}",
                                f"{total:.2f}",
                            ]
                        )
                    )
                    order_writer.write("\n")

                    order_id += 1