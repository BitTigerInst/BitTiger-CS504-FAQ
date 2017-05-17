package io.bittiger.adindex;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;


public class AdsSelector {
    private static AdsSelector instance = null;
    //private int EXP = 7200;
    private String mMemcachedServer;
    private int mMemcachedPortal;
    private String mysql_host;
    private String mysql_db;
    private String mysql_user;
    private String mysql_pass;
    MemcachedClient cache;

    protected AdsSelector(String memcachedServer,int memcachedPortal,String mysqlHost,String mysqlDb,String user,String pass)
    {
        mMemcachedServer = memcachedServer;
        mMemcachedPortal = memcachedPortal;
        mysql_host = mysqlHost;
        mysql_db = mysqlDb;
        mysql_user = user;
        mysql_pass = pass;
        String address = mMemcachedServer + ":" + mMemcachedPortal;
        try {
            cache = new MemcachedClient(new ConnectionFactoryBuilder().setDaemon(true).setFailureMode(FailureMode.Retry).build(), AddrUtil.getAddresses(address));
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static AdsSelector getInstance(String memcachedServer,int memcachedPortal,String mysqlHost,String mysqlDb,String user,String pass) {
        if(instance == null) {
            instance = new AdsSelector(memcachedServer, memcachedPortal,mysqlHost,mysqlDb,user,pass);
        }
        return instance;
    }
    public List<Ad> selectAds(Query query)
    {
        List<Ad> adList = new ArrayList<Ad>();
        HashMap<Long,Integer> matchedAds = new HashMap<Long,Integer>();
        try {
            for(int i = 0; i < query.getTermList().size();i++)
            {
                String queryTerm = query.getTerm(i);
                System.out.println("selectAds queryTerm = " + queryTerm);
                @SuppressWarnings("unchecked")
                Set<Long>  adIdList = (Set<Long>)cache.get(queryTerm);
                if(adIdList != null && adIdList.size() > 0)
                {
                    for(Object adId : adIdList)
                    {
                        Long key = (Long)adId;
                        if(matchedAds.containsKey(key))
                        {
                            int count = matchedAds.get(key) + 1;
                            matchedAds.put(key, count);
                        }
                        else
                        {
                            matchedAds.put(key, 1);
                        }
                    }
                }
            }
            for(Long adId:matchedAds.keySet())
            {
                System.out.println("selectAds adId = " + adId);
                MySQLAccess mysql = new MySQLAccess(mysql_host, mysql_db, mysql_user, mysql_pass);
                Ad.Builder  ad = mysql.getAdData(adId);
                double relevanceScore = matchedAds.get(adId) * 1.0 / ad.getKeyWordsList().size();
                System.out.println("relevanceScore = " + relevanceScore);
                ad.setRelevanceScore(relevanceScore);
                System.out.println("selectAds pClick = " + ad.getPClick());
                System.out.println("selectAds relevanceScore = " + ad.getRelevanceScore());
                adList.add(ad.build());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return adList;
    }
}
