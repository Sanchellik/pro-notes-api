package ru.gozhan.pronotesapi.test.constant;

public class TokenConstant {

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String FRESH_ACCESS_TOKEN = """
            eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYW5jaGVsbGlrIiwiaWQiOjEsInJvbGVzIjpbIlJPTEVfVVNFUiJd\
            LCJleHAiOjUzMzAzMjI4MTd9.esl_Rxbcx9WPoy7jIDXW1ySaf5MU0RLGGie_bFozr6UAEbzCOoTkus1cNhtO\
            ygUheHU0wqmCtQQwq1m9_jm2Vw""";
    public static final String FRESH_REFRESH_TOKEN = """
            eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYW5jaGVsbGlrIiwiaWQiOjEsImV4cCI6NDMyMjMyMjczOH0.30dr\
            5O3yIVNRwMi5sQ7EWdQIVkbNzvLT1lQ8SuDf0RUHtCOizsoERskBvFmmJXeSD-Hu8tELDMFD1JW_mPAvgw""";

    public static final String EXPIRED_ACCESS_TOKEN = """
            eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYW5jaGVsbGlrIiwiaWQiOjEsInJvbGVzIjpbIlJPTEVfVVNFUiJd\
            LCJleHAiOjE3MzA0MDUzNTJ9.AFq8ZbJrSUFGsyu-auVuIfZPFqP6ifV4Rn1l-9TwYBjg8Qh5UOu2i_EKEZMP\
            C-6reqCLX1M9xHMJhnAuksoKJw""";
    public static final String EXPIRED_REFRESH_TOKEN = """
            eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYW5jaGVsbGlrIiwiaWQiOjEsImV4cCI6MTczMDQwNTM1M30._m7k\
            9GLxBUobu9X61FbMYvTF5trC9WNx7DGmnTfP7G3ss5HNRe2o3ENH43jdzoMTv4IUGeHH2yk3hv36brjY4Q""";

}
