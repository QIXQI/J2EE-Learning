/**
 * 管理员后端处理
 */
package club.search;

import java.io.*;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.net.URLEncoder;
import java.net.URLDecoder;

import javax.servlet.annotation.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import javax.sql.DataSource;