package co.hyp3r.data;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import twitter4j.*;
import twitter4j.auth.AccessToken;

import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main( String[] args ) throws Exception {
        System.out.println( "Hello World!" );


        Twitter twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer("T6wJsHz0yMA4wYoH3mPGLWpps",
                                 "4jmII7jO43c0ny0TAB1OOft7RD2nfePITlRlSrlEWNSxeBpFKD");
        twitter.setOAuthAccessToken(new AccessToken("61770290-QAC4slMohuEdrUO9nNyJwMW8mVmQZBVrf3eXEsR5g",
                                                    "ZUThVil4Iirfqes4ZhG9PP3eV8DKT3DugbPwu2eT6fDyD"));


        GeometryFactory factory = new GeometryFactory();
        List<Coordinate> points = new ArrayList<>();
        String[] locations = "33.77092939840706,-84.39995176524116:33.771321812530424,-84.39879305094672:33.77128613859347,-84.39003832072211:33.77121479067501,-84.3717563840766:33.760262180717895,-84.37205679148627:33.760179256872796,-84.37461779927821:33.76010789970673,-84.37865184163661:33.75971543423157,-84.38182757711024:33.759786791724366,-84.39191268300624:33.7588591396848,-84.39225600576015:33.75550523701633,-84.39787791585536:33.75457753864408,-84.39839289998622:33.753828236476664,-84.40053866719813:33.753863917680754,-84.40277026509852:33.76335459062299,-84.40298484181972:33.76360433098408,-84.40165446614833:33.764317870862975,-84.39732001638026:33.76635142692541,-84.39749167775722:33.769954627055824,-84.39895079946132".split(":");
        for(String loc : locations) {
            String[] point = loc.split(",");
            points.add(new Coordinate(Double.valueOf(point[0]), Double.valueOf(point[1])));
        }
        if (!points.get(0).equals2D(points.get(points.size()-1))) {
            points.add(points.get(0));
        }

        Polygon fence = factory.createPolygon(factory.createLinearRing(points.toArray(new Coordinate[0])));

        Query qry = new Query().geoCode(new GeoLocation(33.764860, -84.38820), 2.19145, "km").count(1000);
        //qry = new Query("#spartanracenationalspark").count(100);


        for (Status tweet : twitter.search(qry).getTweets()) {
            if (tweet.getGeoLocation() == null) {
                continue;
            }
            Point p = factory.createPoint(new Coordinate(tweet.getGeoLocation().getLatitude(), tweet.getGeoLocation().getLongitude()));
            if (fence.contains(p)) {
                System.out.println(tweet.getId() + " ===> " + tweet.getCreatedAt() + "  " + tweet.getText() + " [ by " + tweet.getUser().getScreenName() + " @ " + tweet.getGeoLocation() + "]");
            }
        }
       System.out.println("\n\nok?\n\n");
        //for (Status tweet : twitter.getUserTimeline("mac01021")) {
        //    System.out.println(tweet.getId() + " ===> " + tweet.getCreatedAt()+ "  " + tweet.getText() + " [ by " + tweet.getUser().getScreenName()  + "]");
        //}
    }

}
