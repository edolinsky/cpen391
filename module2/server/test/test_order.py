from __future__ import absolute_import
from flask_testing import LiveServerTestCase
import requests
import server


class OrderTests(LiveServerTestCase):
    order_endpoint = "/order"

    def create_app(self):
        app = server.app
        app.config['TESTING'] = True

        # Set to 0 to have the OS pick the port.
        app.config['LIVESERVER_PORT'] = 0
        return app

    @classmethod
    def setUpClass(cls):
        pass

    @classmethod
    def tearDownClass(cls):
        pass

    def setUp(self):
        pass

    def test_get_success(self):
        pass

    def test_get_csv_success(self):
        pass

    def test_get_no_restaurant(self):
        pass

    def test_get_no_customer(self):
        pass

    def test_get_no_table(self):
        pass

    def test_post_no_restaurant(self):
        pass

    def test_post_no_customer(self):
        pass

    def test_post_no_table(self):
        pass

    def test_post_restaurant_nonexistent(self):
        pass

    def test_post_customer_nonexistent(self):
        pass

    def test_post_table_nonexistent(self):
        pass

    def test_post_no_items(self):
        pass

    def test_post_empty_items(self):
        pass
