### 지하철 노선도 프로젝트
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션의 클론 프로젝트입니다
<br><br>
미션수행기간 피드백받았는 부분을 개선하여 다시 구현해보았습니다

### Getting Started

### Install
#### npm 설치
```
cd frontend
npm install
```
> `frontend` 디렉토리에서 수행해야 합니다.

### Usage
#### webpack server 구동
```
npm run dev
```
#### application 구동
```
./gradlew bootRun
```

#### 요구사항
- [ ] 지하철 노선 기능
  - [ ] 노선 추가
  - [ ] 노선 조회
  - [ ] 노선 수정
  - [ ] 노선 삭제

#### 학습내용
- lombok에 있는 builder로 객체를 생성할 경우 필드검증을 할 수 없습니다
  - 생성과정에서 검증없이 사용한다면 db에 저장하는 과정에서 발생하는 예외를 따로 처리해야합니다