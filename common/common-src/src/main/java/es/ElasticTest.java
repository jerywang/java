/*
curl -H "Content-Type:application/json" -XPUT 'http://localhost:9200/hello/user/1' -d '{"name":"jerry","age":"22"}'
curl -H "Content-Type:application/json" -XPUT 'http://localhost:9200/hello/user/2' -d '{"name":"tom","age":"22"}'
curl -H "Content-Type:application/json" -XPUT 'http://localhost:9200/hello/user/3' -d '{"name":"andy","age":"22"}'
curl -H "Content-Type:application/json" -XPUT 'http://localhost:9200/hello/user/4' -d '{"name":"cat","age":"22"}'
curl -H "Content-Type:application/json" -XPUT 'http://localhost:9200/hello/user/5' -d '{"name":"lucy","age":"22"}'
curl -H "Content-Type:application/json" -XPUT 'http://localhost:9200/hello/user/6' -d '{"name":"lily","age":"22"}'
curl -H "Content-Type:application/json" -XPUT 'http://localhost:9200/hello/user/7' -d '{"name":"amily","age":"20"}'
curl -H "Content-Type:application/json" -XPUT 'http://localhost:9200/hello/user/8' -d '{"name":"angle","age":"22"}'
curl -H "Content-Type:application/json" -XPUT 'http://localhost:9200/hello/user/9' -d '{"name":"siri","age":"18"}'
curl -H "Content-Type:application/json" -XPUT 'http://localhost:9200/hello/user/10' -d '{"name":"bee","age":"22"}'
curl -H "Content-Type:application/json" -XPUT 'http://localhost:9200/hello/user/11' -d '{"name":"张三","age":"22"}'
curl -H "Content-Type:application/json" -XPUT 'http://localhost:9200/hello/user/12' -d '{"name":"李四","age":"22"}'
curl -H "Content-Type:application/json" -XPUT 'http://localhost:9200/hello/user/13' -d '{"name":"刘六","age":"22"}'
curl -H "Content-Type:application/json" -XPUT 'http://localhost:9200/hello/user/14' -d '{"name":"王琪","age":"22"}'
curl -H "Content-Type:application/json" -XPUT 'http://localhost:9200/hello/user/15' -d '{"name":"汪洋","age":"22"}'
curl -H "Content-Type:application/json" -XPUT 'http://localhost:9200/hello/user/16' -d '{"name":"张峰","age":"22"}'
curl -H "Content-Type:application/json" -XPUT 'http://localhost:9200/hello/user/17' -d '{"name":"白河","age":"20"}'
curl -H "Content-Type:application/json" -XPUT 'http://localhost:9200/hello/user/18' -d '{"name":"都司","age":"22"}'
curl -H "Content-Type:application/json" -XPUT 'http://localhost:9200/hello/user/19' -d '{"name":"李曼","age":"18"}'
curl -H "Content-Type:application/json" -XPUT 'http://localhost:9200/hello/user/20' -d '{"name":"王杰","age":"22"}'
 */
package es;

/*
 * ESTest
 *
 * @author Wang Guoxing
 */

import java.net.InetAddress;
import java.util.concurrent.Callable;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class ElasticTest {

    public static void main(String[] args) throws Exception {
        //        RestClient restClient = RestClient.builder(
        //                new HttpHost("es.kuai.baidu.com", 80, "http")).build();
        //        Response response = restClient.performRequest("GET", "/"); // (1)
        ////        {"query":{"match":{"departure_station_name":"上海"}}}
        ////        Response response = restClient.performRequest("GET", "/bdbus_index/_search", );
        //
        //        System.out.println(EntityUtils.toString(response.getEntity()));
        //        restClient.close();
        Settings settings = Settings.builder()
                .put("cluster.name", "elasticsearch")
                .put("client.transport.sniff", false)
                .build();

        final TransportClient client = new PreBuiltTransportClient(settings).addTransportAddresses(
                new TransportAddress(InetAddress.getByName("localhost"),9300));
        //        List<DiscoveryNode> nodes = client.listedNodes();
        //        System.out.println(nodes);
        //        ExecutorService service = Executors.newFixedThreadPool(10);
        //        List<Future> futures = Lists.newArrayList();
        //        for (int i = 1; i <= 20; i++) {
        //            futures.add(service.submit(new Query(i, client)));
        //        }
        //        for (Future future : futures) {
        //            future.get();
        //        }
        //        service.shutdown();

        //        SearchResponse response = client.prepareSearch("index1", "index2")
        //                .setTypes("type1", "type2")
        //                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
        //                .setQuery(QueryBuilders.termQuery("multi", "test"))                 // Query
        //                .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))     // Filter
        //                .setFrom(0).setSize(60).setExplain(true)
        //                .get();

        SearchResponse response = client.prepareSearch("hello")
                .setTypes("user")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.matchPhraseQuery("name", "张三"))
                //                .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(28))
                //                .setFrom(0).setSize(60).setExplain(true)
                .get();
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
        client.close();
    }

    public static class Query implements Callable<String> {
        private int i;
        private TransportClient client;

        public Query(int i, TransportClient client) {
            this.i = i;
            this.client = client;
        }

        @Override
        public String call() {
            GetResponse response = client.prepareGet("hello", "user", String.valueOf(i)).get();
            System.out.println(response);
            return response.toString();
        }
    }
}
