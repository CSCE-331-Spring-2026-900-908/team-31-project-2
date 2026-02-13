import random
from datetime import datetime, timedelta
with open("orders.csv", "w") as order_writer:
    with open("order_details.csv", "w") as detail_writer:
        order_id = 0
        detail_id = 0
        new_year = datetime(2026, 1, 1)
        menu_prices = [5.99, 4.99, 4.99, 5.99, 3.99, 3.99, 5.99, 4.99, 3.99, 4.99, 5.99, 5.99, 4.99, 3.99, 2.99, 3.99, 4.99, 3.99, 2.99, 3.99]
        for day in range(365):
            for order in range(50):
                employee_id = random.randint(0, 30)
                date_time = new_year + timedelta(day, random.randint(0, 32400), 0, 0, 0, 9)
                numItems = random.randint(1, 4)
                items = []
                for i in range(numItems):
                    items.append(random.randint(0, 19))
                    detail_writer.write(",".join([str(detail_id), str(order_id), str(items[i]), str(menu_prices[items[i]]), "Snapshot"]))
                    detail_writer.write("\n")
                    detail_id += 1
                total_tax = sum(menu_prices[item] for item in items)
                order_writer.write(",".join([str(order_id), str(employee_id), str(date_time), str(total_tax), f"{total_tax*1.0825:.2f}"]))
                order_writer.write("\n")
                order_id += 1