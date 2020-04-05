package com.zsc.servicehi;

import brave.sampler.Sampler;
import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Level;
import java.util.logging.Logger;

@EnableEurekaClient
@RestController
@EnableHystrix  //开启断路器
@EnableHystrixDashboard
@EnableCircuitBreaker
@EnableFeignClients
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ImportAutoConfiguration(PageHelperAutoConfiguration.class)
public class ServiceHiApplication {

    /**
     * 访问下面地址能看见白熊：
     * http://localhost:8763/hystrix
     * 先访问一下这个服务,再访问断路器地址,才能看见数据：
     * http://localhost:8763/hi
     * 断路器访问地址 ：
     * http://localhost:8763/actuator/hystrix.stream
     *
     * @param args
     */

    public static void main(String[] args) {
        SpringApplication.run(ServiceHiApplication.class, args);
    }

    private static final Logger LOG = Logger.getLogger(ServiceHiApplication.class.getName());


    @Autowired
    private RestTemplate restTemplate;

    //    @LoadBalanced
//    @Bean
//    public RestTemplate getRestTemplate(){
//        return new RestTemplate();
//    }
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        //防止超时
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        //建立连接所用的时间 5s
        simpleClientHttpRequestFactory.setConnectTimeout(60000);
        //服务器读取可用资源的时间 10s
        simpleClientHttpRequestFactory.setReadTimeout(60000);
        return new RestTemplate(simpleClientHttpRequestFactory);
    }

    @Bean
    public ServletRegistrationBean getServlet() {
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/actuator/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }

    @Value("${server.port}")
    String port;

    @RequestMapping("/hi")
    @HystrixCommand(fallbackMethod = "hiError")
    public String home(@RequestParam(value = "name", defaultValue = "zsc") String name) {
        return "hi " + name + " ,i am from port:" + port;
    }

    public String hiError(String name) {
        return "hi," + name + ",sorry,error!";
    }

    @RequestMapping("/hiTrack")
    public String callHome() {
        LOG.log(Level.INFO, "calling trace service-hi  ");
        return restTemplate.getForObject("http://localhost:8989/miya", String.class);
    }

    @RequestMapping("/info")
    public String info() {
        LOG.log(Level.INFO, "calling trace service-hi ");
        return "i'm service-hi";
    }

    @GetMapping("/service")
    public String productService() {
        return "Product Service is called...";
    }

    @Bean
    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }


    /**
     * 毕设 start
     */
//    @RequestMapping("/getCity")
//    public ResponseResult index(@RequestParam String city){
//        GetPollutantData getData = new GetPollutantData();
//        PollutionEpisode pollutionEpisode = getData.getCityPollutionEpisode(city);
//        ResponseResult result = new ResponseResult();
//        result.setData(pollutionEpisode);
//        return result;
//    }

}
