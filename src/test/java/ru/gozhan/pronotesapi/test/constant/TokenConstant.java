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

    public static final String SECRET = """
            4449a01142f439c579bcb3eee38f8a2150618c54cdd5dd220daae89dd2b0c0a4ab43674bd57ab5a6b344e\
            ab8514eb520085d5edef96a911fba3b94624c5c0fbd93e288d1f3c359550ce2544c99dae3685f0921eeb3\
            c4e78181281a6b7914a0394e5fd0011afcc3b027becb4a78fedc39029c57254d2705a09551630fb3ab5f0\
            6db9f1fe7ad2c589d3eabca703bf63133bc12b3fb116e4e4bf51a2ae9dd3606f589ba990eddd8efb6a4ab\
            e238c09452736b6a00d80e4bf3d5812bc3d7d680ca30a74335f092436c849c944c316eca2f002d2f67b7d\
            3b214be68ae641a8474d1a4e57d4356f78c67713f3af6b04010330750900ee59d0216f69d7ad16160ccd5b3\
            """;

}
