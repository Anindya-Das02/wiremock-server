## WireMock Standalone Server

A wiremock standalone server (spring boot) with file based request matching for API testing.

#### Features:
- Easy File based request & response matching. No java coding required.
- Matched file & response definition logging.
- Standalone server.

#### How to use:
1. Create a json file with request mappings (see example in code) & keep it inside `src/resources/BOOT-INF/wiremock/mappings/*` directory. 
2. Create a response json file containing json response-body (to be sent). Keep this response json file inside `src/resources/BOOT-INF/wiremock/__files/*` directory.
3. You can use various request matching strategies to match incoming request, such as `matchesJsonPath`, `headers`, `equalToJson`, & more. _see references below_
4. To change wiremock server port or files location in classpath change the below properties in `application.yaml` file appropriately.
```yaml
wiremock:
  port: <your wiremock port>
  usingFilesUnderClasspath: <your classpath>
```

#### Console Output:
```log
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.2)

2024-01-21T00:21:10.472+05:30  INFO 6956 --- [           main] i.d.a.w.WiremockServerApplication        : Starting WiremockServerApplication using Java 20.0.1 with PID 6956 (E:\spring boot prmgs\wiremock-server\target\classes started by HP in E:\spring boot prmgs\wiremock-server)
2024-01-21T00:21:10.483+05:30  INFO 6956 --- [           main] i.d.a.w.WiremockServerApplication        : No active profile set, falling back to 1 default profile: "default"
2024-01-21T00:21:14.071+05:30  INFO 6956 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
2024-01-21T00:21:14.119+05:30  INFO 6956 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2024-01-21T00:21:14.121+05:30  INFO 6956 --- [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.18]
2024-01-21T00:21:14.526+05:30  INFO 6956 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2024-01-21T00:21:14.535+05:30  INFO 6956 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 3887 ms
2024-01-21T00:21:14.736+05:30  INFO 6956 --- [           main] in.das.app.wiremock.WiremockConfig       : creating wiremock server in port:9090
2024-01-21 00:21:14.872 Verbose logging enabled
2024-01-21T00:21:18.023+05:30  INFO 6956 --- [           main] in.das.app.wiremock.WiremockConfig       : starting wiremock server in port:9090
2024-01-21T00:21:19.035+05:30  INFO 6956 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path ''
2024-01-21T00:21:19.065+05:30  INFO 6956 --- [           main] i.d.a.w.WiremockServerApplication        : Started WiremockServerApplication in 9.974 seconds (process running for 11.145)
2024-01-21T00:21:21.406+05:30  INFO 6956 --- [nio-8080-exec-3] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2024-01-21T00:21:21.406+05:30  INFO 6956 --- [nio-8080-exec-3] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2024-01-21T00:21:21.409+05:30  INFO 6956 --- [nio-8080-exec-3] o.s.web.servlet.DispatcherServlet        : Completed initialization in 3 ms
2024-01-21 00:21:25.436 Request received:
[0:0:0:0:0:0:0:1] - GET /test/api

Host: [localhost:9090]
Connection: [keep-alive]
sec-ch-ua: ["Not_A Brand";v="8", "Chromium";v="120", "Google Chrome";v="120"]
sec-ch-ua-mobile: [?0]
sec-ch-ua-platform: ["Windows"]
Upgrade-Insecure-Requests: [1]
User-Agent: [Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36]
Accept: [text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7]
Sec-Fetch-Site: [none]
Sec-Fetch-Mode: [navigate]
Sec-Fetch-User: [?1]
Sec-Fetch-Dest: [document]
Accept-Encoding: [gzip, deflate, br]
Accept-Language: [en-US,en;q=0.9]



Matched response definition:
{
  "status" : 200,
  "bodyFileName" : "app1/app1_get_test1_response.json",
  "headers" : {
    "Content-Type" : "application/json"
  }
}

Response:
HTTP/1.1 200
Content-Type: [application/json]
Matched-Stub-Id: [f231bb4c-4a7b-4c97-9085-bd56911235b8]

```

#### References:
- [wiremock request matching docs](https://wiremock.org/docs/request-matching/)