import uuid


class Order:

    def __init__(self, table_id):
        self.table_id = table_id

    def get_order(self, order_id):
        pass

    def place_order(self, order):
        pass

    def update_order(self, order):
        pass

    def cancel_order(self):
        pass

    @staticmethod
    def generate_id():
        return str(uuid.uuid4().hex)[-10:]
