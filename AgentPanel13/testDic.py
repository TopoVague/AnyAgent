BlehDic= {
	'Hue': 1234,
	'ho ho': 123
}


print(BlehDic)
print(BlehDic['Hue'])

DicStr= str(BlehDic)

print(DicStr)

import ast

DicBack= ast.literal_eval(DicStr)
print(BlehDic['Hue'])