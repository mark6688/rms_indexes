package com.yunshi.index.dao.esdb;

import org.apache.log4j.Logger;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

/**
 * es连接数据库相关类
 * Created by mk on 2017/11/22.
 */
public class ElasticSearchJdbc {

    private static final Logger logger = Logger.getLogger("ElasticSearchJdbc");

    private  String cluster_name = null;// 实例名称
    private  String cluster_serverip = null;// elasticSearch服务器ip
    private  String indexname = null;// 索引名称
    private TransportClient client = null;
    private  String analyzer_serverip = null;

    public  String getCluster_name() {return cluster_name;}
    public  void setCluster_name(String cluster_name) {this.cluster_name = cluster_name;}
    public  String getCluster_serverip() {return cluster_serverip;}
    public  void setCluster_serverip(String cluster_serverip) {this.cluster_serverip = cluster_serverip;}
    public  String getIndexname() {return indexname;}
    public  void setIndexname(String indexname) {this.indexname = indexname;}

    public String getAnalyzer_serverip() {
        return analyzer_serverip;
    }

    public void setAnalyzer_serverip(String analyzer_serverip) {
        this.analyzer_serverip = analyzer_serverip;
    }

    private void init() {
        // 设置集群名称
        Settings settings = Settings.builder().put("cluster.name", cluster_name).build();
        // 创建client
        client = new PreBuiltTransportClient(settings);

        if (this.cluster_name == null || "".equals(this.cluster_name)) {
            throw new EsdbException("ES未配置实例名称！");
        }
        if (this.cluster_serverip == null || "".equals(this.cluster_serverip)) {
            throw new EsdbException("ES未配置服务器IP！");
        }
        if (this.indexname == null || "".equals(this.indexname)) {
            throw new EsdbException("ES未配置索引名称！");
        }

        try {
            // 增加地址和端口
            String[] ipArry = cluster_serverip.split(",");
            for (String ip : ipArry) {
                String[] ipPort = ip.split(":");
                client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ipPort[0]),Integer.valueOf(ipPort[1])));
            }

        }catch (Exception e){
            logger.error("class : ElasticSearchJdbc --> method : getClient 创建ES客户端连接异常！");
            e.printStackTrace();
        }
    }

    /**
     * 返回一个ElasticSearch的连接客户端
     *
     * @return client
     */
    public TransportClient getClient() {
        if(client==null) {
            init();
        }
        return client;
    }
}
