### 지하철 노선도 프로젝트
[ATDD 미션](https://github.com/next-step/atdd-subway-service) 실습을 위한 지하철 노선도 애플리케이션의 클론 프로젝트입니다
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
- [X] 지하철 노선 기능
  - [X] 노선 추가
  - [X] 노선 조회
  - [X] 노선 수정
  - [X] 노선 삭제
  
#### 회고
- lombok에 있는 builder로 객체를 생성할 경우 필드검증을 할 수 없습니다
  - 생성과정에서 검증없이 사용한다면 db에 저장하는 과정에서 발생하는 예외를 따로 처리해야합니다
  
- 다른 애그리거트끼리는 간접참조가 선호됩니다. 하지만 현실의 비즈니스는 그렇게 간단하지 않습니다.
  - 간접참조로 했을때의 비즈니스 로직이 생각보다 길어질 수 있습니다. 어느 정도의 타협이 필요합니다.
  - 라인에서 구간을 추가하는 부분을 아이디로 구현한다면 로직이 생각보다 길어질 수 있습니다
  - 물론 상한선을 정해놓는것도 필요합니다. 저의 경우 바운디드 컨텍스트내부에서는 필요에따라 연관관계를 맺을수 있게 구현했습니다

- DB에 종속적인 연관관계를 맺을 필요는 없지만 DB와 다르게 구현할 필요는 없습니다.
  - OneToMany관계는 단방향으로 구현할 경우 외래키가 db의 테이블과 다른 엔티티에 존재합니다. 관리가 어렵습니다

- 테스트 시나리오가 길어질때 구조화 하기 위해서 다이나믹 테스트를 사용해볼 수 있습니다.
    - 지하철 구간 추가 시나리오는 역 추가 - 노선 추가 - 구간 추가 - 노선 조회의 시나리오가 필요합니다
    - 각 시나리오를 테스트 코드를 통해 구현하면 대략 50줄이 넘어가는 코드이고 이는 길어질수록 가독성이 떨어지는 부분입니다.
    - 가독성 문제를 해결하기위해 다이나믹 테스트를 사용해볼수 있습니다. 다이나믹 테스트는 말그대로 내부 테스트 데이터가 런타임때 변하는 테스트입니다.
    - 사실 위에 기술했던 정의는 기존에 정적테스트로도 충분히 구현할 수 있지만 다이나믹 테스트의 장점은 테스트내부에서 테스트간 경계를 만들어 주는 점입니다.
    - 최종적으로 Stream을 반환하는 다이나믹테스트는 Stream의 요소들에 개별적인 인수테스트를 추가할 수 있습니다. 그리고 각 요소들에 라벨링을 부여할 수 있기떄문에 테스트 의 가독성이 좋아집니다
    - 다만 각 Stream의 요소들은 데이터를 공유할 수 없고, Stream내부에 데이터는 상수만 허용합니다
    - 테스트내부에 테스트간 경계를 만들어 주는 방법은 다이나믹테스트 외에 Nested를 활용한 이너클래스를 이용하는 방법도 있습니다.