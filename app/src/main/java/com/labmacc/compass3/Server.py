from flask import Flask,jsonify


from flask_restful import Resource, Api, reqparse


app = Flask(__name__)
api = Api(app)


parser = reqparse.RequestParser()

parser.add_argument('time',required=True)
parser.add_argument('azimuth',type=float,required=True)

data=[]

class myHUB(Resource):

    def get(self):
        #return 'ok'
        return jsonify(data)

    def post(self):
        args = parser.parse_args()
        t = args['time']
        a = args['azimuth']
        data.append({'time':t,'azimuth':a})
        return jsonify(len(data))

api.add_resource(myHUB,'/')

if __name__ == '__main__':
    print('starting myHUB api...waiting')
    #app.run(host='0.0.0.0')
