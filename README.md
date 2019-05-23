[![Release](https://jitpack.io/v/djrain/EastarCalendar.svg)](https://jitpack.io/#djrain/EastarCalendar)

## Eastar Calendar?

어느날 갑자기 만들고 싶어진 라이브러리
canvas base로 직접 캘린더를 직접 그림(속도가 빠름)
좀더 빠르도록 개선의 여지가 있지만 현재 상태에서 불편함이 없어서 그냥 사용한다.

## What different Eastar Calendar?

아랍권의 calendar 형태를 지원합니다.
canvas에서 날짜 표시부를 표현 하도록 했기때문에 빠릅니다.
구현부에서 drawer를 재공 받아서 표현 하기때문에 모든 형태의 표현이 가능합니다.

## What's new?

2019.5
처음 배포함 

## How...

### Gradle with jitpack

#### Add it in your root build.gradle at the end of repositories:
```javascript
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
#### Add the dependency
```javascript
dependencies {
        implementation 'com.github.djrain:EastarCalendar:1.0.1'
}
```

## License 
 ```code
Copyright 2019 eastar Jeong

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
