from PIL import Image
from urllib import request
from io import BytesIO
import sys
import os

# 시스템 매개변수에서 이미지 url 추출
url = sys.argv[1]
# 시스템 매개변수에서 저장할 경로 추출
path = sys.argv[2]
# 시스템 매개변수에서 저장할 이미지 이름 추출
title = sys.argv[3]

# 이미지 불러오기
res = request.urlopen(url).read()
img = Image.open(BytesIO(res))

img = img.convert("RGBA")
datas = img.getdata()

newData = []
cutOff = 240

for item in datas:
    if item[0] >= cutOff and item[1] >= cutOff and item[2] >= cutOff:
        newData.append((255, 255, 255, 0))
        # RGB의 각 요소가 모두 cutOff 이상이면 transparent하게 바꿔줍니다.
    else:
        newData.append(item)
        # 나머지 요소는 변경하지 않습니다.

img.putdata(newData)

# 폴더가 없다면 만들어주기
if not os.path.isdir(path):
  os.mkdir(path)

img.save(path+"/"+title, "PNG") # PNG 포맷으로 저장합니다.

print("completed")