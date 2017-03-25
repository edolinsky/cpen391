from __future__ import absolute_import
from flask_testing import LiveServerTestCase
import requests
import server
from hub import Hub
from user import User


class ServerHubMapTests(LiveServerTestCase):
    server_hub_map_endpoint = "/server_hub_map"

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

