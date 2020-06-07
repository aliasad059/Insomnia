package HttpClient;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * gets an HttpResponse and initialize its fields,as HttpResponse are not serializable
 */
public class Response implements Serializable {
    transient HttpResponse<byte[]>httpResponse;
    private String responseSize,responseTime;
    private Map<String, List<String>> headers;
    private byte[] body;
    private boolean isJson,containsPic;
    private JLabel statusLabel;
    public Response(HttpResponse<byte[]>httpResponse) {
        this.httpResponse = httpResponse;
        headers = new HashMap<>();
        setBody();
        setContainsPic();
        setHeaders();
        setIsJson();
        setResponseSize();
        setStatusLabel();
    }
    private void setStatusLabel() {
        int statusCode = httpResponse.statusCode();
        if (statusCode >= 500) {
            statusLabel = new JLabel(statusCode + " Error");
            statusLabel.setOpaque(true);
            statusLabel.setBackground(Color.RED);
            statusLabel.setForeground(Color.BLACK);
            statusLabel.setToolTipText("Server Error");
        } else if (statusCode >= 400) {
            statusLabel = new JLabel(statusCode + " Error");
            statusLabel.setOpaque(true);
            statusLabel.setBackground(Color.RED);
            statusLabel.setForeground(Color.BLACK);
            statusLabel.setToolTipText("Client Error");
        } else if (statusCode >= 300) {
            statusLabel = new JLabel(statusCode + " Moved");
            statusLabel.setOpaque(true);
            statusLabel.setBackground(Color.ORANGE);
            statusLabel.setForeground(Color.BLACK);
            statusLabel.setToolTipText("Redirection");
        } else if (statusCode >= 200) {
            statusLabel = new JLabel(statusCode + " OK");
            statusLabel.setOpaque(true);
            statusLabel.setBackground(Color.GREEN);
            statusLabel.setForeground(Color.BLACK);
            statusLabel.setToolTipText("Successful");
        }
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public String getResponseSize() {
        return responseSize;
    }

    private void setResponseSize() {
        responseSize =  httpResponse.body().length / 1024 + "kB";
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(double responseTime) {
        String timeSize = " nS";
        if (responseTime > 1000000000) {
            timeSize = " S";
            responseTime /= 1000000000;
        } else if (responseTime > 1000000) {
            timeSize = " mS";
            responseTime /= 1000000;
        } else if (responseTime > 1000) {
            timeSize = " μS";
            responseTime /= 1000;
        }
        this.responseTime = responseTime+timeSize;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    private void setHeaders() {
        this.headers = httpResponse.headers().map();
    }

    public byte[] getBody() {
        return body;
    }

    private void setBody() {
        this.body = httpResponse.body();
    }

    public boolean isJson() {
        return isJson;
    }

    private void setIsJson( ) {
        isJson = httpResponse.headers().map().get("content-type").get(0).contains("application/json");
    }

    public boolean doesContainPic() {
        return containsPic;
    }

    private void setContainsPic() {
        containsPic = httpResponse.headers().map().get("content-type").get(0).contains("image");
    }
}
