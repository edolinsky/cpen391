import uuid


class Order:

    def __init__(self):
        self.id = ''

    def place_order(self):
        pass

    def update_order(self):
        pass

    def add_to_order(self):
        pass

    def cancel_order(self):
        pass


def generate_id():
    return uuid.uuid4()[-10:]
