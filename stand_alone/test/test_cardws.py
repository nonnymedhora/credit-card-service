import requests, json, socket
import mysql.connector
from nose import tools

#adding comment to check for git creds


dbconfig = {
	'user':'bw_ws',
	'password':'bwwsftw',
	'host':'sac_nonprod_db',
	'database':'bw_services_secure'
}


def testGetCard(endpoint, creds):
	req = requests.get(endpoint + creds)
	return req

def testPutCard(endpoint, payload):
	headers = {'content-type': 'application/json'}
	req = requests.post(endpoint,data=json.dumps(payload),headers=headers)
	return req
	
def checkRes(req, expStat=200, isFailureCase=False, failMsg=''):
	print(req.status_code)
	print(req.json())
	tools.eq_(req.status_code,expStat, \
		'Expected status code '+str(expStat)+', got '+str(req.status_code)+' instead.')
	assert req.ok, 'Expected json to set "ok" value to True, it was '+str(req.ok)
	if isFailureCase:
		if failMsg:
			pass
			#print(str(json.loads(req.text)['result']['message']))
			#assert str(json.loads(req.text)['result']['message']).index(failMsg)==0
		return "Fails, as expected; isSuccess: "+str(json.loads(req.text)['result']['isSuccess'])+ \
		" message: "+str(json.loads(req.text)['result']['message'])	
	assert json.loads(req.text)['result']['isSuccess'],\
		'Expected "isSuccess" to be True, it was not; message: '+str(json.loads(req.text)['result']['message'])
	return True

def checkLoc(ip, host):
	print(ip+' '+host)
	print(socket.gethostbyaddr(ip))
	
req = testGetCard(endpoint = 'http://sac_qa_app_ws:8080/services/cards/',\
			creds = '024d6278-5a1b-4b95-bb46-fe1c0d9cbfc9')
checkRes(req)
req.close()

# failure case, invalid card
req = testGetCard(endpoint = 'http://sac_qa_app_ws:8080/services/cards/',\
			creds = 'ZZZd6278-5a1b-4b95-bb46-fe1c0d9cbfc9')
checkRes(req, isFailureCase=True, failMsg='Card does not exist')
req.close()

# failure case, no card id; causes a 405
req = testGetCard(endpoint = 'http://sac_qa_app_ws:8080/services/cards/',\
			creds = '')
#print(req.text)
#assert_raises(ValueError, checkRes, req, expStat=405, isFailureCase=True)
req.close()
			
endpoint = 'http://sac_qa_app_ws:8080/services/cards/'
req = testPutCard(endpoint,\
			payload = {"value":"5115915115915118","audit":{"createdBy":"NOW1BAWA","modifiedBy":"NOW1NOBAWA"},"result":{"message":[{}]}})
checkRes(req, expStat=201)
url = req.headers['location'].strip('http://')
loccolsep = url.index(':')
dnscolsep = endpoint.strip('http://').index(':')
checkLoc(url[:loccolsep], endpoint.strip('http://')[:dnscolsep])
req.close()

#failure case, blank card id
endpoint = 'http://sac_qa_app_ws:8080/services/cards/'
req = testPutCard(endpoint,\
			payload = {"value":"","audit":{"createdBy":"NOW1BAWA","modifiedBy":"NOW1NOBAWA"},"result":{"message":[{}]}})
checkRes(req, expStat=200)
url = req.headers['location'].strip('http://')
loccolsep = url.index(':')
dnscolsep = endpoint.strip('http://').index(':')
checkLoc(url[:loccolsep], endpoint.strip('http://')[:dnscolsep])
req.close()


#cnx = mysql.connector.connect(**dbconfig)
#curA = cnx.cursor()
#query = ('select * from customer_card where card_id="024d6278-5a1b-4b95-bb46-fe1c0d9cbfc9"')
#resultset = curA.execute(query)
#print(resultset)

#print(req.text)
#print(req.json()['result']['isSuccess'])

#print(req.headers)
#print('----------- ADD -------------')
#        aCard.setValue("5115915115915118");//("3411-341-1341-1347");//("5405-2222-2222-2226");//("5111005111051128");;
# value=<16 digit number str>
#        aCard.setCreatedBy("NOW1BAWA");//("AAAMIIIIII");//("UUUUTUUUMEEEEEE");
# createdBy=<str>
#        aCard.setModifiedBy("NOW1NOBAWA");//("MISHTIEEEEE");
# modifiedBy=<str>

#payload = {'value':'5115915115915118','createdBy':'somename','modifiedBy':'othername'}
#payload = {'value':'5115915115915118'}

#note!  createdBy and modifiedBy are within an audit subdict
#payload = {"value":"5115915115915118","audit":{"createdBy":"NOW1BAWA","modifiedBy":"NOW1NOBAWA"},"result":{"message":[{}]}}
#headers = {'content-type': 'application/json'}
#req = requests.post(endpoint,data=json.dumps(payload),headers=headers)
#print(req.status_code)
#print(req.headers)
#print(req.text)
#print(req.content)
#print(req.raw.read(50))