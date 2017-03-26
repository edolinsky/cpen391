import db
import copy
from db.order_db import OrderDb


class Order:
    def __init__(self, restaurant_id):
        self.restaurant_id = restaurant_id
        self.db = OrderDb(user=db.db_user,
                          passwd=db.db_passwd,
                          host=db.db_host,
                          db=db.db_database,
                          port=db.db_port)

    def get_order(self, order_id, restaurant_id, content_type):
        """
        Retrieves all items associated with the specified order id.
        :param order_id:
        :param restaurant_id:
        :param content_type:
        :return:
        """

        # Retrieve order info from database.
        order_info = self.db.get_order(restaurant_id=restaurant_id,
                                       order_id=order_id)

        # If client is asking for CSV, parse values from dictionary into string.
        if content_type == 'text/csv':

            if 'error' in order_info:
                return "error\r\n{}".format(order_info['error'])

            else:
                order_string = ""

                for item in order_info['items']:
                    item_list = [item['name'], item['customer_name'], item['status']]

                    order_string += ",".join(map(str, item_list)) + "\r\n"

                return order_string

        # Otherwise, continue with JSON.
        else:
            return order_info

    def place_order(self, order, customer_id, table_id):
        """
        Creates a series of items corresponding to a single order.
        :param order:
        :param customer_id:
        :param table_id:
        :return:
        """

        # Generate unique order id common to items.
        order_id = self.db.generate_id()

        # Create new list of items, each with their own id.
        updated_items = []
        for item in order['items']:
            item_copy = copy.copy(item)
            item_copy['id'] = self.db.generate_id()
            item_copy['status'] = 'placed'
            updated_items.append(item_copy)

        updated_order = {'items': updated_items, 'restaurant_id': self.restaurant_id,
                         'customer_id': customer_id, 'table_id': table_id,
                         'order_id': order_id}

        # Insert into database.
        return self.db.insert_order(order_info=updated_order)

    def update_order(self, order):
        pass

    def cancel_order(self):
        pass

    def update_status(self, update_info):
        """
        Updates solely the status of each of the order IDs specified in the list
        :param update_info: dict containing list of dicts containing status ID and
        its updated status.
        :return:
        """
        return self.db.update_status(update_info=update_info)
