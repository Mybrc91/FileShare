package com.csyangchsh.fileshare.util;

import com.csyangchsh.fileshare.FSApplication;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpConnectionMetrics;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpServerConnection;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.impl.SocketHttpServerConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.*;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author csyangchsh
 * 14/8/29.
 */
public class MiniFileServer implements Runnable{

    private ServerSocket serversocket;
    private HttpService httpService;

    public MiniFileServer(final int port, File docRoot) throws IOException {
        this.serversocket = new ServerSocket(port);
        HttpRequestHandlerRegistry registry = new HttpRequestHandlerRegistry();
        registry.register("*", new FileHandler(docRoot, FSApplication.fileFactory));
        httpService = new HttpService(new BasicHttpProcessor(), new DefaultConnectionReuseStrategy(),
                new DefaultHttpResponseFactory());
        httpService.setHandlerResolver(registry);
    }

    private void listen() {
        System.out.println("Listening on port " + this.serversocket.getLocalPort());
        while (!Thread.interrupted()) {
            try {
                // Set up HTTP connection
                serversocket.setReuseAddress(true);
                Socket socket = this.serversocket.accept();
                System.out.println("Incoming connection from " + socket.getInetAddress());
                DefaultHttpServerConnection conn = new DefaultHttpServerConnection();
                conn.bind(socket,new BasicHttpParams());
                // Start worker thread
                Thread t = new WorkerThread(this.httpService, conn);
                t.setDaemon(true);
                t.start();
            } catch (InterruptedIOException ex) {
                Util.safeClose(serversocket);
                break;
            } catch (IOException e) {
                System.err.println("I/O error initialising connection thread: "
                        + e.getMessage());
                break;
            }
        }
    }

    @Override
    public void run() {
        listen();
    }

    static class WorkerThread extends Thread {

        private final HttpService httpservice;
        private final HttpServerConnection conn;

        public WorkerThread(
                final HttpService httpservice,
                final HttpServerConnection conn) {
            super();
            this.httpservice = httpservice;
            this.conn = conn;
        }

        @Override
        public void run() {
            System.out.println("New connection thread");
            HttpContext context = new BasicHttpContext(null);
            try {
                while (!Thread.interrupted() && this.conn.isOpen()) {
                    this.httpservice.handleRequest(this.conn, context);
                    conn.shutdown();
                }
            } catch (ConnectionClosedException ex) {
                System.err.println("Client closed connection");
            } catch (IOException ex) {
                System.err.println("I/O error: " + ex.getMessage());
            } catch (HttpException ex) {
                System.err.println("Unrecoverable HTTP protocol violation: " + ex.getMessage());
            } finally {
                try {
                    this.conn.shutdown();
                } catch (IOException ignore) {}
            }
        }

    }

}

