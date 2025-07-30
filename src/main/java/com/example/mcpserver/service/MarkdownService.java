package com.example.mcpserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class MarkdownService {

    @Tool(name = "markdown-service.markdown", description = "Fredt Stone 提供Markdown服务，把MD文档中的内容识别出来，并入库")
    public String markdown(String markdown) {
        log.info("========================收到Markdown信息:{}", markdown);
        String outputFilePath = "F:\\output\\markdown.md";
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(markdown);
            String markdownContent = new String(decodedBytes, StandardCharsets.UTF_8);
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
            writer.write(markdownContent);
            writer.flush();
            writer.close();
            handleMarkdown(outputFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("解析失败：" + e.getMessage());
        }

        return "服务端已收到Markdown信息：【" + outputFilePath + "】";
    }


    /**
     * 从临时保存的Markdown文件中读取数据，并转换Markdown文件
     * @param markdownFilePath
     */
    private void handleMarkdown(String markdownFilePath) {
        try {
            // 创建 FileSystemResource 实例
            Resource fileResource = new FileSystemResource(markdownFilePath);
            MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                    .withHorizontalRuleCreateDocument(true)
                    .withIncludeBlockquote(true)
                    .withIncludeCodeBlock(true)
                    .build();

            List<Document> documents = new MarkdownDocumentReader(fileResource, config).read();
            for (Document document : documents) {
                document.getMetadata().forEach((key, value) -> System.out.println(key + ": " + value));
                System.out.println("Document: " + document.getText());
            }

            // 转换成PDF,WORD
            String markdownContent = FileUtils.readFileToString(fileResource.getFile());
            convertMarkdownToPDF(markdownContent);
            convertMarkdownToWord(markdownContent);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("解析失败：" + e.getMessage());
        }
    }


    private String convertMarkdownToHtml(String markdownContent) {
        try {
            Parser parser = Parser.builder().build();
            Node document = parser.parse(markdownContent);

            HtmlRenderer reader = HtmlRenderer.builder().escapeHtml(true).build();
            String body = reader.render(document);

            String outputFilePath = "F:\\output\\output.html";
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
            writer.write(body);
            writer.flush();
            writer.close();

            System.out.println("Conversion completed successfully.");

            return "<!DOCTYPE html>" +
                    "<html><head><meta charset='UTF-8'>" +
                    "<style>" +
                    "body { font-family: 'Microsoft YaHei', 'SimSun', sans-serif; font-size: 14px; line-height: 1.6; }" +
                    "pre, code { background: #f4f4f4; padding: 4px; font-family: Consolas, monospace; }" +
                    "</style></head><body>" + body + "</body></html>";

        } catch (Exception e) {
            e.printStackTrace();
            log.error("解析失败：" + e.getMessage());
        }
        return "";

    }

    private void convertMarkdownToWord(String markdownContent) {
        String outputPath = "F:\\output\\output.docx";

        try {
            String html = convertMarkdownToHtml(markdownContent);
            try (XWPFDocument doc = new XWPFDocument(); FileOutputStream out = new FileOutputStream(outputPath)) {
                String[] lines = html.replaceAll("<[^>]*>", "").split("\n");
                for (String line : lines) {
                    if (!line.trim().isEmpty()) {
                        XWPFParagraph paragraph = doc.createParagraph();
                        paragraph.createRun().setText(line.trim());
                    }
                }
                doc.write(out);
            }




//            Parser parser = Parser.builder().build();
//            Node document = parser.parse(markdownContent);
//            XWPFDocument doc = new XWPFDocument();
//
//            // 遍历 CommonMark AST
//            document.accept(new AbstractVisitor() {
//                private XWPFParagraph currentParagraph = null;
//
//                @Override
//                public void visit(Heading heading) {
//                    currentParagraph = doc.createParagraph();
//                    currentParagraph.setStyle("Heading" + heading.getLevel()); // 设置 Word 内置标题样式
//                    processChildren(heading, currentParagraph);
//                    currentParagraph = null; // 重置当前段落
//                }
//
//                @Override
//                public void visit(Paragraph paragraph) {
//                    currentParagraph = doc.createParagraph();
//                    processChildren(paragraph, currentParagraph);
//                    currentParagraph = null; // 重置当前段落
//                }
//
//                @Override
//                public void visit(Text text) {
//                    if (currentParagraph != null) {
//                        XWPFRun run = currentParagraph.createRun();
//                        run.setText(text.getLiteral());
//                    }
//                }
//
//                @Override
//                public void visit(StrongEmphasis strongEmphasis) {
//                    if (currentParagraph != null) {
//                        XWPFRun run = currentParagraph.createRun();
//                        run.setBold(true);
//                        // 递归处理粗体内部的文本
//                        processChildren(strongEmphasis, run);
//                    }
//                }
//
//                @Override
//                public void visit(Emphasis emphasis) {
//                    if (currentParagraph != null) {
//                        XWPFRun run = currentParagraph.createRun();
//                        run.setItalic(true);
//                        // 递归处理斜体内部的文本
//                        processChildren(emphasis, run);
//                    }
//                }
//
//                // 对于其他节点类型（如 CodeBlock, ListItem, Link, Image 等），你需要添加相应的 visit 方法
//                // 并使用 XWPFDocument 和 XWPFParagraph 的相应方法来处理它们。
//                // 例如：处理代码块，设置等宽字体
//                @Override
//                public void visit(FencedCodeBlock fencedCodeBlock) {
//                    XWPFParagraph codeParagraph = doc.createParagraph();
//                    XWPFRun codeRun = codeParagraph.createRun();
//                    codeRun.setFontFamily("Consolas"); // 设置等宽字体
//                    codeRun.setText(fencedCodeBlock.getLiteral());
//                    // 可以考虑设置背景色或边框以更好地模拟代码块
//                }
//
//                // 辅助方法，递归处理子节点
//                private void processChildren(Node node, XWPFParagraph paragraph) {
//                    Node current = node.getFirstChild();
//                    while (current != null) {
//                        current.accept(this); // 继续访问子节点
//                        current = current.getNext();
//                    }
//                }
//
//                private void processChildren(Node node, XWPFRun parentRun) {
//                    Node current = node.getFirstChild();
//                    while (current != null) {
//                        if (current instanceof Text) {
//                            parentRun.setText(((Text) current).getLiteral());
//                        } else if (current instanceof StrongEmphasis) {
//                            XWPFRun strongRun = parentRun.getParagraph().createRun();
//                            strongRun.setBold(true);
//                            strongRun.setText(((Text) ((StrongEmphasis) current).getFirstChild()).getLiteral());
//                        } else if (current instanceof Emphasis) {
//                            XWPFRun emphasisRun = parentRun.getParagraph().createRun();
//                            emphasisRun.setItalic(true);
//                            emphasisRun.setText(((Text) ((Emphasis) current).getFirstChild()).getLiteral());
//                        }
//                        // 注意：这里需要更复杂的逻辑来确保 XWPFRun 的正确创建和文本追加
//                        // 通常，你应该在每个 Text 节点创建新的 Run，或者在 Paragraph 级别处理 Runs。
//                        // 这里的递归处理对于内联格式（粗体/斜体）需要仔细设计。
//                        current.accept(this); // 这行是递归的关键，但需要处理好 Run 的上下文
//                        current = current.getNext();
//                    }
//                }
//            });
//
//            // 保存文档
//            try (FileOutputStream out = new FileOutputStream(outputPath)) {
//                doc.write(out);
//            }
//            doc.close();

            log.info("转换成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("解析失败：" + e.getMessage());
        }


//
//        try {
//            String html = convertMarkdownToHtml(markdownContent);
//
//
//            XWPFDocument document = new XWPFDocument();
//            XWPFParagraph paragraph = document.createParagraph();
//
//            XWPFRun run = paragraph.createRun();
//            run.setText(html);
//            FileOutputStream outputStream = new FileOutputStream("F:\\output\\output.docx");
//            document.write(outputStream);
//            outputStream.close();
//            document.close();
//            System.out.println("Conversion completed successfully.");
//            log.info("转换成功");
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("解析失败：" + e.getMessage());
//        }
    }


    private void convertMarkdownToPDF(String markdownContent) {
        String outputPath = "F:\\output\\output.pdf";
        try {
            String html = convertMarkdownToHtml(markdownContent);
            System.out.println(html);
//            OutputStream os = new FileOutputStream(outputPath);



            PDDocument document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page, false, true)) {

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(50, 750);
                contentStream.showText("Hello, this is an example of using PDPageContentStream!");
                contentStream.endText();


                contentStream.beginText();
                contentStream.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(300, 750);
                contentStream.showText("Hello, PDFBox!");
                contentStream.endText();

            } catch (IOException e) {
                e.printStackTrace();
            }

            // 保存PDF文件
            document.save(outputPath);
            document.close();




//            PdfRendererBuilder builder = new PdfRendererBuilder();
//            // 指定 HTML 内容和输出流
//            builder.withHtmlContent(html, null);
//            builder.useFastMode();
//            builder.toStream(os);
//            os.flush();

            // 中文字体支持（确保系统已安装宋体 SimSun 字体）
//            builder.useFont(() -> new File("/usr/share/fonts/simsun.ttc"), "SimSun");  // Linux 示例
            // Windows 下自动使用系统字体（通常不需手动加载）
//            builder.useFont(() -> null, "SimSun");  // 或 "Microsoft YaHei"

//            builder.run();
//            os.close();

            log.info("✅ PDF 生成成功：" + outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
