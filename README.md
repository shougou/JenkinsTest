# JenkinsTest
src目录类似于标准java目录结构。执行流水线时，此目录将添加至类路径calss_path中。

vars 目录定义可从流水线访问的全局变量的脚本。 每个 *.groovy 文件的基名应该是一个 Groovy (~ Java) 标识符, 通常是 camelCased。 匹配 *.txt, 如果存在, 可以包含文档, 通过系统的配置标记格式化从处理 (所以可能是 HTML, Markdown 等，虽然 txt 扩展是必需的)。

resources 目录允许从外部库中使用 libraryResource 步骤来加载有关的非 Groovy 文件。 目前，内部库不支持该特性。
根目录下的其他目录被保留下来以便于将来的增强。

