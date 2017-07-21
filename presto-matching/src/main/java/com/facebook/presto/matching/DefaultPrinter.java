/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.matching;

import com.facebook.presto.matching.pattern.CapturePattern;
import com.facebook.presto.matching.pattern.FilterPattern;
import com.facebook.presto.matching.pattern.TypeOfPattern;
import com.facebook.presto.matching.pattern.WithPattern;

public class DefaultPrinter
        implements PatternVisitor
{
    private StringBuilder result = new StringBuilder();
    private int level = 0;

    public String result()
    {
        return result.toString();
    }

    @Override
    public void visitTypeOf(TypeOfPattern<?> pattern)
    {
        visitPrevious(pattern);
        appendLine("typeOf(%s)", pattern.expectedClass().getSimpleName());
    }

    @Override
    public void visitWith(WithPattern<?> pattern)
    {
        visitPrevious(pattern);
        appendLine("with(%s)", pattern.getProperty()); //TODO provide actual name
        level += 1;
        pattern.getPattern().accept(this);
        level -= 1;
    }

    @Override
    public void visitCapture(CapturePattern<?> pattern)
    {
        visitPrevious(pattern);
        appendLine("capturedAs(%s)", pattern.capture().description());
    }

    @Override
    public void visitFilter(FilterPattern<?> pattern)
    {
        visitPrevious(pattern);
        appendLine("filter(%s)", pattern.predicate());
    }

    private void appendLine(String template, Object... arguments)
    {
        result.append(Util.indent(level, template + "\n", arguments));
    }
}