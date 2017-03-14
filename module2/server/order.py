import db
import copy
from enum import Enum
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

    def get_order(self, order_id):
        return self.db.get_order(order_id=order_id)

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


