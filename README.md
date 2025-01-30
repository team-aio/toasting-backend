### 간략한 커밋 컨벤션
----

|      태그 이름       |                설명                |
|:----------------:|:--------------------------------:|
|       Feat       |            새로운 기능 추가             |
|       Fix        |               버그수정               |
|      Style       | 코드 포맷 수정, 세미 콜론 누락, 코드 수정이 없는 경우 |
|     Comment      |          필요한 주석 추가 및 변경          |
|       Docs       |              문서 수정               |
|      Chore       |           빌드 테스크 업데이트            |
|      Rename      |       파일 혹은 폴더명을 수정하거나 이동        |
|      Remove      |         파일을 삭제하는 작업만 수행          |
|     !HOTFIX      |            급하게 버그 수정             |
| !BREAKING CHANGE |            버그를 고친 경우             |

출처 및 참고 : https://overcome-the-limits.tistory.com/6

### 디렉토리 구조 예시
---
```bash
io
└─ toasting
        ├── domain
        │   ├── member
        │   │   ├── controller
        │   │   │   ├── request
        │   │   │   └── response
        │   │   ├── entity
        │   │   ├── exception
        │   │   ├── repository
        │   │   │   └── dto
        │   │   └── application
        │   │       └── dto
        │   ├── post
        │   │   ├── controller
        │   │   │   ├── request
        │   │   │   └── response
        │   │   ├── entity
        │   │   ├── exception
        │   │   ├── repository
        │   │   │   └── dto
        │   │   └── application
        │   │       └── dto
        │   ├── model
        │  ...
        │
        ├── global
        │   ├── auth
        │   ├── common
        │   ├── config
        │   ├── error
        │   ├── infra
        │   └── util
        │
        └─ infra

``` 
출처 및 참고 : https://cheese10yun.github.io/spring-guide-directory/
