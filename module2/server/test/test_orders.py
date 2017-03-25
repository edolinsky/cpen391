from __future__ import absolute_import
from flask_testing import LiveServerTestCase
import requests
import server


class OrdersTests(LiveServerTestCase):
    orders_endpoint = "/orders"

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

    def test_get_not_supported(self):
        pass

    def test_get_no_restaurant(self):
        pass

    def test_get_restaurant_nonexistent(self):
        pass

    def test_patch_success(self):
        pass

    def test_patch_no_restaurant(self):
        pass

    def test_patch_restaurant_nonexistent(self):
        pass
