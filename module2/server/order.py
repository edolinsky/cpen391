import db
import copy
from db.order_db import OrderDb


class Order:

    def __init__(self, table_id, restaurant_id, customer_id):
        self.table_id = table_id
        self.restaurant_id = restaurant_id
        self.customer_id = customer_id
        self.db = OrderDb(user=db.db_user,
                          passwd=db.db_passwd,
                          host=db.db_host,
                          db=db.db_database,
                          port=db.db_port)

    def get_order(self, order_id, restaurant_id, content_type):
        order_info = self.db.get_order(restaurant_id=restaurant_id, order_id=order_id)
        if content_type == 'text/tab-separated-values':
            if 'error' in order_info:
                return "error\n{}".format(order_info['error'])
            else:
                order_string = ("id\tstatus\tname\tcustomer_name\tprice\t"
                                "type\tmenu_id\tdescription\n")
                for item in order_info['items']:
                    item_list = [item['id'], item['status'], item['name'],
                                 item['customer_name'], item['price'], item['type'],
                                 item['menu_id'], item['description']]
                    order_string += "\t".join(map(str, item_list)) + "\n"

                return order_string
        else:
            return order_info

    def place_order(self, order):
        order_id = self.db.generate_id()

        updated_items = []
        for item in order['items']:
            item_copy = copy.copy(item)
            item_copy['id'] = self.db.generate_id()
            item_copy['status'] = 'placed'
            updated_items.append(item_copy)

        updated_order = {'items': updated_items, 'restaurant_id': self.restaurant_id,
                         'customer_id': self.customer_id, 'table_id': self.table_id,
                         'order_id': order_id}

        return self.db.insert_order(order_info=updated_order)

    def update_order(self, order):
        pass

    def cancel_order(self):
        pass

    def update_status(self, statuses):
        """
        Updates solely the status of each of the order IDs specified in the list
        :param statuses: list of dicts containing status ID and its updated status.
        :return:
        """
        pass
