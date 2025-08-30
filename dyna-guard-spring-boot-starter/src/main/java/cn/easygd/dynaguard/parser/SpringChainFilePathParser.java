package cn.easygd.dynaguard.parser;

import cn.easygd.dynaguard.core.path.ChainFilePathParser;
import cn.easygd.dynaguard.domain.enums.FreeMarkTypeEnum;
import cn.easygd.dynaguard.domain.exception.ValidationChainParserException;
import cn.easygd.dynaguard.utils.FileUtils;
import com.google.common.collect.Lists;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * spring流程文件路径解析器
 *
 * @author VD
 */
public class SpringChainFilePathParser implements ChainFilePathParser {

    /**
     * 解析流程文件路径
     *
     * @param pathList 路径列表
     * @return 绝对路径列表
     */
    @Override
    public List<String> parse(List<String> pathList) {
        List<String> result = Lists.newArrayList();
        for (String path : pathList) {
            // 获取当前文件匹配规则
            String wildcardMatch = FileUtils.getWildcardMatch(path);
            if (FileUtils.isAbsolutePath(path)) {
                // 本地文件解析
                List<String> localFilePathList = FileUtils.getFileAbsolutePath(path, wildcardMatch).stream()
                        .map(source -> ResourceUtils.FILE_URL_PREFIX + source)
                        .collect(Collectors.toList());
                result.addAll(localFilePathList);
            } else {
                // 以下为spring部分的兼容逻辑
                if (!path.startsWith(ResourceUtils.CLASSPATH_URL_PREFIX)
                        && !path.startsWith(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX)) {
                    path = ResourceUtils.CLASSPATH_URL_PREFIX + path;
                }
                try {
                    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
                    Resource[] resources = resolver.getResources(path);
                    for (Resource resource : resources) {
                        result.add(resource.getURL().toString());
                    }
                } catch (IOException e) {
                    throw new ValidationChainParserException("path parse exception : " + e.getMessage(), e);
                }
            }
        }
        return result;
    }

    /**
     * 获取解析器名称
     *
     * @return 解析器名称
     */
    @Override
    public String getParserName() {
        return FreeMarkTypeEnum.SPRING.getType();
    }
}
